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
public class FcompInstruction extends ThreeAddressIlocInstruction {
  public FcompInstruction(VirtualRegisterOperand source1,
      VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public FcompInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "fcomp";
  }

  public static String getHash(VirtualRegisterOperand src1,
      VirtualRegisterOperand src2) {
    return "fcomp" + src1.toString() + src2.toString();
  }

  protected int getOperandType(Operand operand) {
    if (dest == operand)
      return Operand.INTEGER_TYPE;
    else
      return Operand.FLOAT_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    FcompInstruction inst = new FcompInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FcompInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
