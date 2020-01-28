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
public abstract class VariableAddressIlocInstruction extends IlocInstruction {
  Vector<Operand> operands;

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.print("\t" + getOpcode() + "\t");
    for (int i = 0; i < operands.size() - 1; i++) {
      Operand operand = (Operand) operands.elementAt(i);
      pw.print(operand.toString() + ", ");
    }

    if (operands.size() > 0) {
      Operand operand = (Operand) operands.elementAt(operands.size() - 1);
      pw.print(operand.toString() + " ");
    }

    emitInstSpecific(pw);
  }

  public Vector<Operand> getOperands() {
    return operands;
  }

  public boolean operandIsRValue(Operand operand) {
    return operands.contains(operand);
  }

  public boolean operandIsLValue(Operand operand) {
    return false;
  }

  protected void emitInstSpecific(PrintWriter pw) {
    pw.println("");
  }
}
