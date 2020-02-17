package middleEnd.iloc;

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
public abstract class TwoAddressIlocInstruction extends IlocInstruction {
  Operand source;
  Operand dest;

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source.toString() + " " + " => " + dest.toString());
    return rep;
  }

  public Operand getSource() {
    return source;
  }

  public Operand getDestination() {
    return dest;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    setType(typeMap, source);
    setType(typeMap, dest);
  }

  public boolean operandIsLValue(Operand operand) {
    return operand == dest;
  }

  public boolean operandIsRValue(Operand operand) {
    return operand == source;
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
    if (index == 0)
      source = operand;
    else if (index == 1)
      dest = operand;
    rValues.set(index, operand);
  }

  public void replaceLValue(Operand operand) {
    dest = operand;
    lValue = (VirtualRegisterOperand) operand;
  }

  public boolean isExpression() {
    return true;
  }

  @Override
  public boolean isNecessary() {
    return false;
  }
}
