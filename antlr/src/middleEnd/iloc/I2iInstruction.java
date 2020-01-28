package middleEnd.iloc;

import java.util.Vector;

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
public class I2iInstruction extends CopyInstruction {
  public I2iInstruction(VirtualRegisterOperand source,
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
    return "i2i";
  }

  public static String getHash(VirtualRegisterOperand src1) {
    return "i2i"+src1.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
   Integer lOpVal = (Integer)constVals.elementAt(0);

   ImmediateOperand source = new ConstantOperand(lOpVal.intValue());

   return new LoadIInstruction(source,(VirtualRegisterOperand)dest);
  }
}
