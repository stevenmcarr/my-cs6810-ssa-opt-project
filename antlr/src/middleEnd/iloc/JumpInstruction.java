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
public class JumpInstruction extends OneAddressIlocInstruction {
  public JumpInstruction(VirtualRegisterOperand source) {
    this.source = source;
    rValues.add(source);
  }

  public JumpInstruction() {
  }

  public String getTargetLabel() {
    return ((LabelOperand) source).getLabel();
  }

  public void setTargetLabel(LabelOperand target) {
    source = target;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "jump";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";
    rep += ("\t" + getOpcode() + "\t-> " + source.toString());
    return rep;
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isExpression() {
    return false;
  }

  @Override
  public boolean isNecessary() {
    return true;
  }

  @Override
  public IlocInstruction deepCopy() {
    JumpInstruction inst = new JumpInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(JumpInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
