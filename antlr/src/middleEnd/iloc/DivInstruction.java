package middleEnd.iloc;

import java.util.Vector;

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
public class DivInstruction extends ThreeAddressIlocInstruction {
  public DivInstruction(VirtualRegisterOperand source1,
      VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public DivInstruction() {
  }

  public String getOpcode() {
    return "div";
  }

  public static String getHash(VirtualRegisterOperand src1,
      VirtualRegisterOperand src2) {
    return "div" + src1.toString() + src2.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);
    Integer rOpVal = (Integer) constVals.elementAt(1);

    ImmediateOperand source = new ConstantOperand(lOpVal.intValue() / rOpVal.intValue());

    return new LoadIInstruction(source, (VirtualRegisterOperand) dest);
  }

  public IlocInstruction constantPropagate(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);
    Integer rOpVal = (Integer) constVals.elementAt(1);

    if (lOpVal == null) {
      ConstantOperand source2 = new ConstantOperand(rOpVal.intValue());
      return new DivIInstruction((VirtualRegisterOperand) source1, source2, (VirtualRegisterOperand) dest);
    } else
      return this;
  }

  @Override
  public IlocInstruction deepCopy() {
    DivInstruction inst = new DivInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(DivInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
