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
public class RshiftInstruction extends ThreeAddressIlocInstruction {
  public RshiftInstruction(VirtualRegisterOperand source1,
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
    return "rshift";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
   Integer lOpVal = (Integer)constVals.elementAt(0);
   Integer rOpVal = (Integer)constVals.elementAt(1);

   ImmediateOperand source = new ConstantOperand(lOpVal.intValue() >> rOpVal.intValue());

   return new LoadIInstruction(source,(VirtualRegisterOperand)dest);
  }

  public IlocInstruction constantPropagate(Vector<Integer> constVals) {
    Integer lOpVal = (Integer)constVals.elementAt(0);
    Integer rOpVal = (Integer)constVals.elementAt(1);

    if (lOpVal == null) {
      ConstantOperand source2 = new ConstantOperand(rOpVal.intValue());
      return new RshiftIInstruction((VirtualRegisterOperand)source1,source2,(VirtualRegisterOperand)dest);
    }
    else
      return this;
  }
}
