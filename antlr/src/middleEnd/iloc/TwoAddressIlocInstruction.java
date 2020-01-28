package middleEnd.iloc;

import java.io.PrintWriter;
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

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + source.toString() + " " + " => " + dest.toString());
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
}
