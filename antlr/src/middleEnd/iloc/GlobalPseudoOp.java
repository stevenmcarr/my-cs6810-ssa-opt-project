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
public class GlobalPseudoOp extends PseudoOpInstruction {
  String name;
  int size;
  int align;

  public GlobalPseudoOp(String name, int size, int align) {
    this.name = name;
    this.size = size;
    this.align = align;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".global";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + name + ", " + size + ", " + align);
    return rep;
  }

  public String getName() {
    return name;
  }

  public int getSize() {
    return size;
  }

  public int getAlign() {
    return align;
  }

}
