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
public class SubIInstruction extends ThreeAddressIlocInstruction {
  public SubIInstruction(VirtualRegisterOperand source1,
                         ImmediateOperand source2,
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
    return "subI";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction optimizeIdentity() {
    if (source2 instanceof ConstantOperand &&
        ((ConstantOperand)source2).getValue() == 0)
      return new I2iInstruction((VirtualRegisterOperand)source1,
                                (VirtualRegisterOperand)dest);
    else
      return null;
  }
}
