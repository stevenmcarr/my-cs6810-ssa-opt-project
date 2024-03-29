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
public class StoreInstruction extends TwoAddressIlocInstruction {
  public StoreInstruction(VirtualRegisterOperand source, VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    rValues.add(source);
    rValues.add(dest);
  }

  public StoreInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "store";
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
    StoreInstruction inst = new StoreInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(StoreInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
