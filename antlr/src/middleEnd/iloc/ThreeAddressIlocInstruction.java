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
public abstract class ThreeAddressIlocInstruction extends IlocInstruction {
  Operand source1;
  Operand source2;
  Operand dest;

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source1.toString() + ", " + source2.toString() + " => " + dest.toString());
    return rep;
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

}
