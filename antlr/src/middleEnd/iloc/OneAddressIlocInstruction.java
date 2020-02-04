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
public abstract class OneAddressIlocInstruction extends IlocInstruction {
  Operand source;

  public abstract String getOpcode();

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source.toString());
    return rep;
  }

  public Operand getOperand() {
    return source;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    setType(typeMap, source);
  }

  public boolean operandIsLValue(Operand operand) {
    return false;
  }

  public boolean operandIsRValue(Operand operand) {
    return operand == source;
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
    if (index == 0) {
      source = operand;
      rValues.set(index, operand);
    }
  }

  public void replaceLValue(Operand operand) {
  }
}
