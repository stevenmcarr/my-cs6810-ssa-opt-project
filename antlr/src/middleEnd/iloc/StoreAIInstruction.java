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
public class StoreAIInstruction extends ThreeAddressIlocInstruction {
  public StoreAIInstruction(VirtualRegisterOperand source1, ImmediateOperand source2, VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    rValues.add(dest);
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
    return "storeAI";
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + source1.toString() + " => " + dest.toString() + ", " + source2.toString());
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
}
