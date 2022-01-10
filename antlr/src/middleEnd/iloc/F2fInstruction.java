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
public class F2fInstruction extends CopyInstruction {
  public F2fInstruction(VirtualRegisterOperand source,
      VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  public F2fInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "f2f";
  }

  protected int getOperandType(Operand operand) {
    return Operand.FLOAT_TYPE;
  }

  public static String getHash(VirtualRegisterOperand source) {
    return "f2f" + source.toString();
  }

  @Override
  public IlocInstruction deepCopy() {
    F2fInstruction inst = new F2fInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(F2fInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
