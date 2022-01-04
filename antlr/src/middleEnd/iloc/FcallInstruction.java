package middleEnd.iloc;

import java.util.*;

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
public class FcallInstruction extends InvocationInstruction {

  private VirtualRegisterOperand returnRegister;

  public FcallInstruction(LabelOperand name, Vector<Operand> parameters, VirtualRegisterOperand returnReg) {
    operands = new Vector<Operand>();
    operands.add(name);
    for (int i = 0; i < parameters.size(); i++) {
      Operand parameter = (Operand) parameters.elementAt(i);
      operands.add(parameter);
      rValues.add(parameter);
    }
    returnRegister = returnReg;
    lValue = returnRegister;
  }

  public VirtualRegisterOperand getReturnRegister() {
    return returnRegister;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "fcall";
  }

  @Override
  protected String getStringRepSpecific() {
    return ("\t => " + returnRegister.toString());
  }
}
