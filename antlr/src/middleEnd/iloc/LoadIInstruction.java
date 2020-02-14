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
public class LoadIInstruction extends CopyInstruction {
  public LoadIInstruction(ImmediateOperand source, VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "loadI";
  }

  public static String getHash(ImmediateOperand src1) {
    return "loadI" + src1.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isExpression() {
    return true;
  }
}
