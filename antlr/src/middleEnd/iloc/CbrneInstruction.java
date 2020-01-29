package middleEnd.iloc;

import java.io.PrintWriter;
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
public class CbrneInstruction extends TwoAddressIlocInstruction {
  public CbrneInstruction(VirtualRegisterOperand source1, LabelOperand dest) {
    super();
    this.source = source1;
    this.dest = dest;
    rValues.add(dest);
    rValues.add(source);
  }

  public String getTargetLabel() {
    return ((LabelOperand) dest).getLabel();
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + source.toString() + " " + " -> " + dest.toString());
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.iloc.IlocInstruction method
   */
  public String getOpcode() {
    return "cbrne";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);

    if (lOpVal.intValue() == 0)
      return new JumpIInstruction((LabelOperand) dest);
    else
      return new NopInstruction();
  }
}
