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

  public String getTargetLabel() {
    return ((LabelOperand) source).getLabel();
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
}
