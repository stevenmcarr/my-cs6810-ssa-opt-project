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
public class JumpIInstruction extends OneAddressIlocInstruction {
  public JumpIInstruction(ImmediateOperand source) {
    this.source = source;
    rValues.add(source);
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "jumpI";
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t-> " + source.toString());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }
}
