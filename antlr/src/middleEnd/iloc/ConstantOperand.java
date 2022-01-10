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
public class ConstantOperand extends ImmediateOperand {

  private int value;

  public ConstantOperand(int value) {
    this.value = value;
  }

  public ConstantOperand() {
  }

  public ConstantOperand copy() {
    return new ConstantOperand(value);
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public Operand deepCopy() {
    Operand op = new ConstantOperand();
    copyInstanceVars(op);
    return op;
  }

  protected void copyInstanceVars(ConstantOperand copy) {
    copy.value = value;
    super.copyInstanceVars(copy);
  }
}
