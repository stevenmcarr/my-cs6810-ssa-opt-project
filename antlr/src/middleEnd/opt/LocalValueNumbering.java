package middleEnd.opt;

import java.util.*;

import middleEnd.*;
import middleEnd.iloc.*;
import middleEnd.utils.*;

/**
 * <p>
 * Title: Nolife Compiler
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author Steve Carr
 * @version 1.0
 */
public class LocalValueNumbering {

  IlocGenerator ilocGen;

  private int nextValueNumber;

  public LocalValueNumbering(IlocGenerator ilocGen) {
    this.ilocGen = ilocGen;
  }

  public void performValueNumbering() {
    Vector<IlocRoutine> routines = ilocGen.getRoutines();
    BitSet liveVariables = new BitSet();

    for (int i = 0; i < routines.size(); i++) {
      IlocRoutine routine = (IlocRoutine) routines.elementAt(i);
      liveVariables.clear();
      buildLiveVariableRegisterSet(routine, liveVariables);
      Vector<BasicBlock> blocks = routine.getBasicBlocks();
      for (int j = 0; j < blocks.size(); j++) {
        BasicBlock block = (BasicBlock) blocks.elementAt(j);
        valueNumber(block);
        removeDeadCode(block, liveVariables);
      }
    }
  }

  private void buildLiveVariableRegisterSet(IlocRoutine routine, BitSet live) {
    Vector<BasicBlock> blocks = routine.getBasicBlocks();
    for (int j = 0; j < blocks.size(); j++) {
      BasicBlock block = (BasicBlock) blocks.elementAt(j);
      IlocInstruction inst = block.getFirstInst();
      IlocInstruction endInst = block.getLastInst().getNextInst();

      /**
       * Go through list of instructions. Assume target of all copy instructions will
       * be a variable register. This is conservative.
       */
      while (inst != endInst) {
        if (inst instanceof I2iInstruction || inst instanceof F2fInstruction)
          live.set(inst.getLValue().getRegisterId());
        inst = inst.getNextInst();
      }
    }

  }

  private void valueNumber(BasicBlock block) {
    Hashtable<String, Integer> constantTable = new Hashtable<String, Integer>();
    Hashtable<String, ValueTableEntry> valueTable = new Hashtable<String, ValueTableEntry>();
    Hashtable<String, ValueTableEntry> loadTable = new Hashtable<String, ValueTableEntry>();
    nextValueNumber = 0;

    IlocInstruction inst = block.getFirstInst();
    IlocInstruction endInst = block.getLastInst().getNextInst();

    while (inst != endInst) {
      IlocInstruction nextInst = inst.getNextInst();
      Vector<Integer> opValueNums = getValueNumbers(valueTable, inst.getRValues());
      inst = performConstantPropagation(valueTable, constantTable, inst, opValueNums);
      inst = checkIdentity(valueTable, inst, opValueNums);
      inst.updateCommutative(opValueNums);
      String hashKey = inst.getHashKey(opValueNums);
      RegisterValueTableEntry valueEntry = (RegisterValueTableEntry) valueTable.get(hashKey);

      //
      // Do not optimize load, store and call instructions
      //
      if (valueEntry != null && !(inst instanceof InvocationInstruction) && !inst.isMemoryStoreInstruction()) {
        if (inst.getLValue().getRegisterId() == valueEntry.getVirtualRegister())
          ilocGen.removeInst(inst);
        else {
          VirtualRegisterOperand lValue = inst.getLValue();
          IlocInstruction newInst;
          if (lValue.getType() == Operand.INTEGER_TYPE)
            newInst = new I2iInstruction(new VirtualRegisterOperand(valueEntry.getVirtualRegister()), lValue.copy());
          else
            newInst = new F2fInstruction(new VirtualRegisterOperand(valueEntry.getVirtualRegister()), lValue.copy());
          ilocGen.replaceInst(inst, newInst);
          valueTable.put(inst.getLValue().toString(), valueEntry.copy());
        }
      } else {
        if (inst instanceof InvocationInstruction) {
          valueTable.clear();
          constantTable.clear();
          loadTable.clear();
        } else if (inst.isMemoryStoreInstruction()) {
          Enumeration<String> keys = loadTable.keys();
          while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            valueTable.remove(key);
          }
          loadTable.clear();
        }
        VirtualRegisterOperand lValue = inst.getLValue();
        if (lValue != null) {
          int valNum = getNewValueNumber(inst, opValueNums);
          valueTable.put(hashKey, new RegisterValueTableEntry(valNum, lValue.getRegisterId()));
          valueTable.put(lValue.toString(), new RegisterValueTableEntry(valNum, lValue.getRegisterId()));
          if (inst instanceof LoadIInstruction) {
            ImmediateOperand source = (ImmediateOperand) ((LoadIInstruction) inst).getSource();
            if (source instanceof ConstantOperand)
              constantTable.put(Integer.toString(valNum), ((ConstantOperand) source).getValue());
          } else if (inst.isMemoryLoadInstruction())
            loadTable.put(hashKey, new RegisterValueTableEntry(valNum, lValue.getRegisterId()));
        }
      }

      inst = nextInst;
    }
  }

  private Vector<Integer> getValueNumbers(Hashtable<String, ValueTableEntry> valueTable, Vector<Operand> operands) {
    Vector<Integer> valueNums = new Vector<Integer>();
    for (int i = 0; i < operands.size(); i++) {
      Operand operand = (Operand) operands.elementAt(i);
      valueNums.add(getOperandValueNumber(valueTable, operand));
    }
    return valueNums;
  }

  private int getOperandValueNumber(Hashtable<String, ValueTableEntry> valueTable, Operand operand) {
    ValueTableEntry valueEntry = (ValueTableEntry) valueTable.get(operand.toString());
    int valueNumber;

    if (valueEntry == null) {
      valueNumber = nextValueNumber++;

      if (operand instanceof VirtualRegisterOperand)
        valueTable.put(operand.toString(),
            new RegisterValueTableEntry(valueNumber, ((VirtualRegisterOperand) operand).getRegisterId()));
      else if (operand instanceof ConstantOperand)
        valueTable.put(operand.toString(),
            new ConstantValueTableEntry(valueNumber, ((ConstantOperand) operand).getValue()));
      else if (operand instanceof LabelOperand)
        valueTable.put(operand.toString(), new LabelValueTableEntry(valueNumber, ((LabelOperand) operand).getLabel()));
      else {
        System.err.println("Invalid operand in value table: " + operand);
        System.exit(-1);
      }
    } else
      valueNumber = valueEntry.getValueNumber();

    return valueNumber;
  }

  private IlocInstruction performConstantPropagation(Hashtable<String, ValueTableEntry> valueTable,
      Hashtable<String, Integer> constantTable, IlocInstruction inst, Vector<Integer> valueNumbers) {

    IlocInstruction returnInst = inst;

    boolean allConstant = true;
    boolean someConstant = false;
    Vector<Integer> constVals = new Vector<Integer>();
    constVals.setSize(valueNumbers.size());
    for (int i = 0; i < valueNumbers.size(); i++) {
      Integer cVal = (Integer) constantTable.get(((Integer) valueNumbers.elementAt(i)).toString());
      if (cVal != null) {
        constVals.set(i, cVal);
        someConstant = true;
      } else
        allConstant = false;
    }
    if (allConstant) {
      IlocInstruction newInst = inst.constantFold(constVals);
      if (newInst != null) {
        ilocGen.replaceInst(inst, newInst);
        returnInst = newInst;
        Vector<Integer> newValueNums = getValueNumbers(valueTable, newInst.getRValues());
        valueNumbers.removeAllElements();
        valueNumbers.addAll(newValueNums);
      }
    } else if (someConstant) {

      IlocInstruction newInst = inst.constantPropagate(constVals);

      if (newInst != null) {
        ilocGen.replaceInst(inst, newInst);
        returnInst = newInst;
      }
    }

    return returnInst;
  }

  public IlocInstruction checkIdentity(Hashtable<String, ValueTableEntry> valueTable, IlocInstruction inst,
      Vector<Integer> valueNumbers) {
    IlocInstruction identityInst = inst.optimizeIdentity();
    if (identityInst == null)
      return inst;
    else {
      ilocGen.replaceInst(inst, identityInst);
      Vector<Integer> newValueNums = getValueNumbers(valueTable, identityInst.getRValues());
      valueNumbers.removeAllElements();
      valueNumbers.addAll(newValueNums);
      return identityInst;
    }
  }

  public int getNewValueNumber(IlocInstruction inst, Vector<Integer> opValueNums) {
    if (inst instanceof CopyInstruction)
      return ((Integer) opValueNums.elementAt(0)).intValue();
    else
      return nextValueNumber++;
  }

  private void removeDeadCode(BasicBlock block, BitSet liveVariables) {
    IlocInstruction inst = block.getLastInst();
    IlocInstruction endInst = block.getFirstInst().getPrevInst();

    BitSet live = (BitSet) liveVariables.clone();

    while (inst != endInst) {
      IlocInstruction prevInst = inst.getPrevInst();
      VirtualRegisterOperand lValue = inst.getLValue();

      if (lValue == null)
        updateLiveSet(live, inst.getRValues());
      else if (live.get(lValue.getRegisterId())) {
        live.clear(lValue.getRegisterId());
        updateLiveSet(live, inst.getRValues());
      } else
        ilocGen.removeInst(inst);
      inst = prevInst;
    }
  }

  private void updateLiveSet(BitSet live, Vector<Operand> rValues) {
    for (int i = 0; i < rValues.size(); i++) {
      Operand operand = (Operand) rValues.elementAt(i);
      if (operand instanceof VirtualRegisterOperand)
        live.set(((VirtualRegisterOperand) operand).getRegisterId());
    }
  }
}
