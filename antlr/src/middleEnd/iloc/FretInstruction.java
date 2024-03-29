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
public class FretInstruction extends OneAddressIlocInstruction {
  public FretInstruction(Operand source) {
    this.source = source;
    rValues.add(source);
  }

  public FretInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "fret";
  }

  protected int getOperandType(Operand operand) {
    return Operand.FLOAT_TYPE;
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
    FretInstruction inst = new FretInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FretInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
