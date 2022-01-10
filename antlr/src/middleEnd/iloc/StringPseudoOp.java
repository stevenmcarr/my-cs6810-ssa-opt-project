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
public class StringPseudoOp extends PseudoOpInstruction {

  String name;
  String val;

  public StringPseudoOp(String name, String val) {
    this.name = name;
    this.val = val;
  }

  public StringPseudoOp() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".string";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + name + ", \"" + val + "\"");
    return rep;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return val;
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    StringPseudoOp inst = new StringPseudoOp();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(StringPseudoOp inst) {
    inst.name = new String(name);
    inst.val = new String(val);
    super.copyInstanceVars(inst);
  }
}
