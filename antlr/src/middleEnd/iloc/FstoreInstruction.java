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
public class FstoreInstruction extends TwoAddressIlocInstruction {
  public FstoreInstruction(VirtualRegisterOperand source, VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    rValues.add(source);
    rValues.add(dest);
  }

  public FstoreInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "fstore";
  }

  protected int getOperandType(Operand operand) {
    if (source == operand)
      return Operand.FLOAT_TYPE;
    else
      return Operand.INTEGER_TYPE;
  }

  public static String getHash(VirtualRegisterOperand source) {
    return "fstore" + source.toString();
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
    FstoreInstruction inst = new FstoreInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FstoreInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
