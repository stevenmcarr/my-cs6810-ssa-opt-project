package middleEnd.iloc;

import java.util.Hashtable;

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
public abstract class NoAddressIlocInstruction extends IlocInstruction {

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode());
    return rep;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
  }

  public boolean operandIsLValue(Operand operand) {
    return false;
  }

  public boolean operandIsRValue(Operand operand) {
    return false;
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
  }

  public void replaceLValue(Operand operand) {
  }

  public boolean isExpression() {
    return false;
  }

  @Override
  public boolean isNecessary() {
    return false;
  }

  protected void assignLRToRValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {
  }

  protected void assignLRToLValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {
  }

  protected void copyInstanceVars(IlocInstruction copy) {
    super.copyInstanceVars(copy);
  }
}
