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
public class FloatPseudoOp extends PseudoOpInstruction {

  String name;
  float val;

  public FloatPseudoOp(String name, float val) {
    this.name = name;
    this.val = val;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".float";
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + name + ", " + val);
  }

  public String getName() {
    return name;
  }

  public float getValue() {
    return val;
  }

  protected int getOperandType(Operand operand) {
    return Operand.FLOAT_TYPE;
  }
}
