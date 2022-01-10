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
public class IretInstruction extends OneAddressIlocInstruction {
  public IretInstruction(Operand source) {
    this.source = source;
    rValues.add(source);
  }

  public IretInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "iret";
  }

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
    IretInstruction inst = new IretInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(IretInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
