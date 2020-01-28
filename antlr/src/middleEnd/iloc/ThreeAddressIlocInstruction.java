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
public abstract class ThreeAddressIlocInstruction extends IlocInstruction {
  Operand source1;
  Operand source2;
  Operand dest;

  public void emit(PrintWriter pw) {
    if (label != null)
      pw.print(label + ":");

    pw.println("\t" + getOpcode() + "\t" + source1.toString() + ", " + source2.toString() + " => " + dest.toString());
  }

  public Operand getLeftOperand() {
    return source1;
  }

  public Operand getRightOperand() {
    return source2;
  }

  public Operand getDestination() {
    return dest;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    setType(typeMap, source1);
    setType(typeMap, source2);
    setType(typeMap, dest);
  }

  public boolean operandIsLValue(Operand operand) {
    return operand == dest;
  }

  public boolean operandIsRValue(Operand operand) {
    return (operand == source1 || operand == source2);
  }
}
