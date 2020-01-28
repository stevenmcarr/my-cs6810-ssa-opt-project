package middleEnd.iloc;

/**
 * <p>Title: Nolife Compiler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Steve Carr
 * @version 1.0
 */
public class F2iInstruction extends TwoAddressIlocInstruction {
  public F2iInstruction(VirtualRegisterOperand source,
                        VirtualRegisterOperand dest) {
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
    return "f2i";
  }

  public static String getHash(VirtualRegisterOperand source) {
    return "f2i"+source.toString();
  }

  protected int getOperandType(Operand operand) {
    if (dest == operand)
      return Operand.INTEGER_TYPE;
    else
      return Operand.FLOAT_TYPE;
  }
}
