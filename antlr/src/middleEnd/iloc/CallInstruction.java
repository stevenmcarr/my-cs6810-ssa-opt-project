package middleEnd.iloc;

import java.util.Vector;
import java.util.Hashtable;

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
public class CallInstruction extends InvocationInstruction {

  public CallInstruction(LabelOperand name, Vector<Operand> parameters) {
    operands = new Vector<Operand>();
    operands.add(name);
    rValues.add(name);
    for (int i = 0; i < parameters.size(); i++) {
      Operand parameter = (Operand) parameters.elementAt(i);
      operands.add(parameter);
      rValues.add(parameter);
    }
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "call";
  }

  @Override
  protected String getStringRepSpecific() {
    return "";
  }
}
