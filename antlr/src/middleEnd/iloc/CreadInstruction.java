package middleEnd.iloc;

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
public class CreadInstruction extends OneAddressIlocInstruction {
  public CreadInstruction(VirtualRegisterOperand source) {
    this.source = source;
    lValue = source;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.iloc.IlocInstruction method
   */
  public String getOpcode() {
    return "cread";
  }

  /**
   * getOperandType
   *
   * @param operand Operand
   * @return int
   * @todo Implement this middleEnd.iloc.IlocInstruction method
   */
  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isExpression() {
    return false;
  }

  @Override
  public boolean isNecessary() {
    return true;
  }

  @Override
  protected void assignLRToRValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {

  }

  @Override
  protected void assignLRToLValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {
    if (vr == source) {
      source = lro;
      lValue = lro;
    }
  }
}
