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
public class I2fInstruction extends CopyInstruction {
  public I2fInstruction(VirtualRegisterOperand source, VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  public I2fInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "i2f";
  }

  public static String getHash(VirtualRegisterOperand source) {
    return "i2f" + source.toString();
  }

  protected int getOperandType(Operand operand) {
    if (operand == source)
      return Operand.INTEGER_TYPE;
    else
      return Operand.FLOAT_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    I2fInstruction inst = new I2fInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(I2fInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
