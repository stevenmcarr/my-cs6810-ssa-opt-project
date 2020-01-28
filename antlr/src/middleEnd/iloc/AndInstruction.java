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
public class AndInstruction extends ThreeAddressIlocInstruction {

  public AndInstruction(VirtualRegisterOperand source1,
                        VirtualRegisterOperand source2,
                        VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "and";
  }

  public static String getHash(VirtualRegisterOperand src1,
                               VirtualRegisterOperand src2) {
    return "and"+src1.toString()+src2.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
   Integer lOpVal = (Integer)constVals.elementAt(0);
   Integer rOpVal = (Integer)constVals.elementAt(1);

   ImmediateOperand source = new ConstantOperand(lOpVal.intValue() & rOpVal.intValue());

   return new LoadIInstruction(source,(VirtualRegisterOperand)dest);
  }
}
