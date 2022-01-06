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
public class AddInstruction extends ThreeAddressIlocInstruction {
  public AddInstruction(VirtualRegisterOperand source1,
      VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public AddInstruction() {
  }

  public String getOpcode() {
    return "add";
  }

  public static String getHash(VirtualRegisterOperand src1,
      VirtualRegisterOperand src2) {
    return "addI" + src1.toString() + src2.toString();
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);
    Integer rOpVal = (Integer) constVals.elementAt(1);

    ImmediateOperand source = new ConstantOperand(lOpVal.intValue() + rOpVal.intValue());

    return new LoadIInstruction(source, (VirtualRegisterOperand) dest);
  }

  public IlocInstruction constantPropagate(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);
    Integer rOpVal = (Integer) constVals.elementAt(1);

    if (lOpVal == null) {
      ConstantOperand source2 = new ConstantOperand(rOpVal.intValue());
      return new AddIInstruction((VirtualRegisterOperand) source1, source2, (VirtualRegisterOperand) dest);
    } else {
      ConstantOperand source1 = new ConstantOperand(lOpVal.intValue());
      return new AddIInstruction((VirtualRegisterOperand) source2, source1, (VirtualRegisterOperand) dest);
    }
  }

  public void updateCommutative(Vector<Integer> valueNums) {
    Integer lOpValNum = (Integer) valueNums.elementAt(0);
    Integer rOpValNum = (Integer) valueNums.elementAt(1);

    if (rOpValNum.intValue() < lOpValNum.intValue()) {
      Operand temp = source1;
      source1 = source2;
      source2 = temp;
      valueNums.set(0, rOpValNum);
      valueNums.set(1, lOpValNum);
    }
  }

  @Override
  public IlocInstruction deepCopy() {
    AddInstruction inst = new AddInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(AddIInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
