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
  protected String label = null;

  protected IlocInstruction nextInst = null;
  protected IlocInstruction prevInst = null;
  protected BasicBlock block = null;

  protected VirtualRegisterOperand lValue = null; // a VirtualRegisterOperand
  protected Vector<Operand> rValues = new Vector<Operand>(); // only VirtualRegisterOperands

  public abstract String getOpcode();

  public abstract void emit(PrintWriter pw);

  public abstract void setOperandTypes(Hashtable<String, Integer> typeHash);

  protected abstract int getOperandType(Operand operand);

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public IlocInstruction getPrevInst() {
    return prevInst;
  }

  public IlocInstruction getNextInst() {
    return nextInst;
  }

  public void setPrevInst(IlocInstruction inst) {
    prevInst = inst;
  }

  public void setNextInst(IlocInstruction inst) {
    nextInst = inst;
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
    return (this instanceof JumpIInstruction || this instanceof JumpInstruction || this instanceof CbrInstruction
        || this instanceof CbrneInstruction);
  }

  public boolean isConditionalBranchInstruction() {
    return (this instanceof CbrInstruction || this instanceof CbrneInstruction);
  }

  public boolean isUnconditionalBranchInstruction() {
    return (this instanceof JumpIInstruction || this instanceof JumpInstruction);
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

  public abstract boolean operandIsLValue(Operand operand);

  public abstract boolean operandIsRValue(Operand operand);
}
