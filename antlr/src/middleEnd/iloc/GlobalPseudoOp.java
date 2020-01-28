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
public class GlobalPseudoOp extends PseudoOpInstruction {
  String name;
  int size;
  int align;

  public GlobalPseudoOp(String name, int size, int align) {
    this.name = name;
    this.size = size;
    this.align = align;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".global";
  }

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + name + ", " + size + ", " + align);
  }

  public String getName() {
    return name;
  }

  public int getSize() {
    return size;
  }

  public int getAlign() {
    return align;
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }
}
