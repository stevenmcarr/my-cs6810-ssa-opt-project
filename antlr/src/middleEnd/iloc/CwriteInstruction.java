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
public class CwriteInstruction extends OneAddressIlocInstruction {
  public CwriteInstruction(VirtualRegisterOperand source) {
    this.source = source;
    rValues.add(source);
  }

  public CwriteInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.iloc.IlocInstruction method
   */
  public String getOpcode() {
    return "cwrite";
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
  public IlocInstruction deepCopy() {
    CwriteInstruction inst = new CwriteInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(CwriteInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
