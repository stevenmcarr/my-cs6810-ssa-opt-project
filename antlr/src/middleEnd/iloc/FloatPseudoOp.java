package middleEnd.iloc;

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
public class FloatPseudoOp extends PseudoOpInstruction {

  String name;
  float val;

  public FloatPseudoOp(String name, float val) {
    this.name = name;
    this.val = val;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".float";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";
    rep += ("\t" + getOpcode() + "\t" + name + ", " + val);
    return rep;
  }

  public String getName() {
    return name;
  }

  public float getValue() {
    return val;
  }

}
