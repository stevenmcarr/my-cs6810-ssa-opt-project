package middleEnd.iloc;

import java.io.PrintWriter;

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
public class StoreAOInstruction extends ThreeAddressIlocInstruction {
  public StoreAOInstruction(VirtualRegisterOperand source1, VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    rValues.add(dest);
    rValues.add(source1);
    rValues.add(source2);
  }

  public StoreAOInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "storeAO";
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + source1.toString() + " => " + source2.toString() + ", " + dest.toString());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isExpression() {
    return false;
  }

  @Override
  public boolean isNecessary() {
    return true;
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
    if (index == 0)
      dest = operand;
    else if (index == 1)
      source1 = operand;
    else
      source2 = operand;
    rValues.set(index, operand);
  }

  @Override
  public IlocInstruction deepCopy() {
    StoreAOInstruction inst = new StoreAOInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(StoreAOInstruction inst) {
    super.copyInstanceVars(inst);
  }

}
