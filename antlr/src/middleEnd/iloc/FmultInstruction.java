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
public class FmultInstruction extends ThreeAddressIlocInstruction {
  public FmultInstruction(VirtualRegisterOperand source1,
      VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public FmultInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   */
  public String getOpcode() {
    return "fmult";
  }

  public static String getHash(VirtualRegisterOperand src1,
      VirtualRegisterOperand src2) {
    return "fmult" + src1.toString() + src2.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.FLOAT_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    FmultInstruction inst = new FmultInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FmultInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
