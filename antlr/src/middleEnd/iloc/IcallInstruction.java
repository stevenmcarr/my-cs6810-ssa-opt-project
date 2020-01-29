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
public class IcallInstruction extends InvocationInstruction {
  private VirtualRegisterOperand returnRegister;
  private static int counter = 0; // each call is unique when hashing

  public IcallInstruction(LabelOperand name, Vector<Operand> parameters, VirtualRegisterOperand returnReg) {
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
    return "icall";
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    for (int i = 1; i < operands.size(); i++) {
      VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(i);
      Integer typeVal = (Integer) typeMap.get(vr.toString());
      vr.setType(typeVal.intValue());
    }
    setType(typeMap, returnRegister);
  }

  public int getOperandType(Operand operand) {
    int index = operands.indexOf(operand);

    if (index != -1) {
      VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(index);
      return vr.getType();
    } else
      return Operand.INTEGER_TYPE;
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";
    rep += ("\t => " + returnRegister.toString());
    return rep;
  }

  public static String getHash(LabelOperand name, Vector<Operand> operands) {
    String key = new String("icall" + name.getLabel()) + (counter++);
    for (int i = 0; i < operands.size(); i++)
      key += ((Operand) operands.elementAt(i)).toString();

    return key;
  }

  @Override
  protected String getStringRepSpecific() {
    return "";
  }
}
