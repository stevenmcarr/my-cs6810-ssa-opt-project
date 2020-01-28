package middleEnd.iloc;

import java.util.Vector;

/**
 * <p>Title: CS4131 Nolife Compiler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class NotInstruction extends TwoAddressIlocInstruction {
  public NotInstruction(VirtualRegisterOperand source,
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
    return "not";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "not"+src.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
   Integer lOpVal = (Integer)constVals.elementAt(0);

   ImmediateOperand source = new ConstantOperand(~lOpVal.intValue() );

   return new LoadIInstruction(source,(VirtualRegisterOperand)dest);
  }
}
