package middleEnd.iloc;

import middleEnd.utils.*;

import java.io.PrintWriter;
import java.util.*;

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
public abstract class IlocInstruction {

  private static int numInst = 0;

  protected String label = null;

  protected BasicBlock block = null;

  protected VirtualRegisterOperand lValue = null; // a VirtualRegisterOperand
  protected Vector<VirtualRegisterOperand> lValues = new Vector<VirtualRegisterOperand>();
  protected Vector<Operand> rValues = new Vector<Operand>(); // only VirtualRegisterOperands

  protected int instId;

  public IlocInstruction() {
    instId = numInst++;
  }

  public int getInstId() {
    return instId;
  }

  public abstract String getOpcode();

  public void emit(PrintWriter pw) {
    pw.println(getStringRep());
  }

  public abstract String getStringRep();

  public abstract void setOperandTypes(Hashtable<String, Integer> typeHash);

  protected abstract int getOperandType(Operand operand);

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setBlock(BasicBlock block) {
    this.block = block;
  }

  public BasicBlock getBlock() {
    return block;
  }

  protected void setType(Hashtable<String, Integer> typeMap, Operand operand) {
    Integer typeVal = (Integer) typeMap.get(operand.toString());

    if (typeVal == null) {
      typeVal = getOperandType(operand);
      typeMap.put(operand.toString(), typeVal);
    }

    operand.setType(typeVal.intValue());
  }

  public Vector<Operand> getRValues() {
    return rValues;
  }

  public VirtualRegisterOperand getLValue() {
    return lValue;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
    return null;
  }

  public IlocInstruction constantPropagate(Vector<Integer> constVals) {
    return null;
  }

  public IlocInstruction optimizeIdentity() {
    return null;
  }

  public void updateCommutative(Vector<Integer> valueNums) {

  }

  public String getHashKey(Vector<Integer> valueNums) {
    String hashKey = getOpcode();
    for (int i = 0; i < valueNums.size(); i++)
      hashKey += ("&" + ((Integer) valueNums.elementAt(i)).toString());

    return hashKey;
  }

  public String getSSAAvailKey() {
    String key = getOpcode();
    for (Operand op : getRValues()) {
      key += op.toString();
    }

    return key;
  }

  public boolean isMemoryLoadInstruction() {
    return (this instanceof LoadInstruction || this instanceof LoadAIInstruction || this instanceof LoadAOInstruction
        || this instanceof FloadInstruction || this instanceof FloadAIInstruction
        || this instanceof FloadAOInstruction);
  }

  public boolean isMemoryStoreInstruction() {
    return (this instanceof StoreInstruction || this instanceof StoreAIInstruction || this instanceof StoreAOInstruction
        || this instanceof FstoreInstruction || this instanceof FstoreAIInstruction
        || this instanceof FstoreAOInstruction || this instanceof CreadInstruction || this instanceof FreadInstruction
        || this instanceof IreadInstruction);
  }

  public boolean isBranchInstruction() {
    return (isConditionalBranchInstruction() || isUnconditionalBranchInstruction());
  }

  public boolean isConditionalBranchInstruction() {
    return (this instanceof CbrInstruction || this instanceof CbrneInstruction);
  }

  public boolean isUnconditionalBranchInstruction() {
    return (this instanceof JumpIInstruction || this instanceof JumpInstruction || this instanceof RetInstruction
        || this instanceof HaltInstruction);
  }

  public boolean isEndInstruction() {
    return (this instanceof RetInstruction || this instanceof HaltInstruction);
  }

  public String getBranchTargetLabel() {
    if (this instanceof JumpIInstruction)
      return ((JumpIInstruction) this).getTargetLabel();
    else if (this instanceof JumpInstruction)
      return ((JumpInstruction) this).getTargetLabel();
    else if (this instanceof CbrInstruction)
      return ((CbrInstruction) this).getTargetLabel();
    else if (this instanceof CbrneInstruction)
      return ((CbrneInstruction) this).getTargetLabel();
    else
      return null;
  }

  public void setBranchTargetLabel(LabelOperand target) {
    if (this instanceof JumpIInstruction)
      ((JumpIInstruction) this).setTargetLabel(target);
    else if (this instanceof JumpInstruction)
      ((JumpInstruction) this).setTargetLabel(target);
    else if (this instanceof CbrInstruction)
      ((CbrInstruction) this).setTargetLabel(target);
    else if (this instanceof CbrneInstruction)
      ((CbrneInstruction) this).setTargetLabel(target);
  }

  public Vector<VirtualRegisterOperand> getAllLValues() {
    if (lValue != null && lValues.isEmpty()) {
      lValues.add(lValue);
    }

    return lValues;
  }

  public abstract boolean isExpression();

  public abstract boolean operandIsLValue(Operand operand);

  public abstract boolean operandIsRValue(Operand operand);

  public abstract void replaceOperandAtIndex(int index, Operand operand);

  public abstract void replaceLValue(Operand operand);

  public void replaceLValue(Operand op, int index) {
    if (lValues != null)
      lValues.set(0, (VirtualRegisterOperand) op);
    replaceLValue(op);
  }

  public abstract boolean isNecessary();

  public void assignLR(VirtualRegisterOperand vr, LiveRangeOperand lro) {
    int index;
    if (lValues != null)
      if ((index = lValues.indexOf(vr)) >= 0) {
        lValues.set(index, lro);
      }
    assignLRToLValue(vr, lro);

    if (rValues != null)
      if ((index = rValues.indexOf(vr)) >= 0)
        rValues.set(index, lro);

    assignLRToRValue(vr, lro);
  }

  protected abstract void assignLRToRValue(VirtualRegisterOperand vr, LiveRangeOperand lro);

  protected abstract void assignLRToLValue(VirtualRegisterOperand vr, LiveRangeOperand lro);

  public abstract IlocInstruction deepCopy();

  protected void copyInstanceVars(IlocInstruction copy) {
    copy.instId = numInst++;

    if (lValue != null)
      copy.lValue = lValue.deepCopy();

    for (VirtualRegisterOperand vr : lValues)
      copy.lValues.add(vr.deepCopy());

    for (Operand op : rValues)
      copy.rValues.add(op.deepCopy());
  }

}
