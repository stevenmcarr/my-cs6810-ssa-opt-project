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

}
