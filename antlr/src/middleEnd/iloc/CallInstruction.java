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

  public CallInstruction() {
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

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    for (int i = 1; i < operands.size(); i++) {
      VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(i);
      Integer typeVal = (Integer) typeMap.get(vr.toString());
      vr.setType(typeVal.intValue());
    }
  }

  public int getOperandType(Operand operand) {
    int index = operands.indexOf(operand);
    VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(index);
    return vr.getType();
  }

  @Override
  protected String getStringRepSpecific() {
    return "";
  }

  @Override
  public IlocInstruction deepCopy() {
    CallInstruction inst = new CallInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(CallInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
