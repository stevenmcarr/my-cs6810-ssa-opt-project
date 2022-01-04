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
public abstract class VariableAddressIlocInstruction extends IlocInstruction {
  Vector<Operand> operands;

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t");
    for (int i = 0; i < operands.size() - 1; i++) {
      Operand operand = (Operand) operands.elementAt(i);
      rep += (operand.toString() + ", ");
    }

    if (operands.size() > 0) {
      Operand operand = (Operand) operands.elementAt(operands.size() - 1);
      rep += (operand.toString() + " ");
    }

    rep += getStringRepSpecific();

    return rep;
  }

  public Vector<Operand> getOperands() {
    return operands;
  }

  protected abstract String getStringRepSpecific();

}
