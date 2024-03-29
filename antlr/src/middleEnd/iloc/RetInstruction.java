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
public class RetInstruction extends NoAddressIlocInstruction {
  public RetInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "ret";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isNecessary() {
    return true;
  }

  @Override
  public IlocInstruction deepCopy() {
    RetInstruction inst = new RetInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(RetInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
