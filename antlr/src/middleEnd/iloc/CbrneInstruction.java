package middleEnd.iloc;

import java.util.Vector;

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
public class CbrneInstruction extends TwoAddressIlocInstruction {
  public CbrneInstruction(VirtualRegisterOperand source1, LabelOperand dest) {
    super();
    this.source = source1;
    this.dest = dest;
    rValues.add(source);
    rValues.add(dest);
  }

  public CbrneInstruction() {
  }

  public String getTargetLabel() {
    return ((LabelOperand) dest).getLabel();
  }

  public void setTargetLabel(LabelOperand target) {
    dest = target;
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source.toString() + " " + " -> " + dest.toString());

    return rep;
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.iloc.IlocInstruction method
   */
  public String getOpcode() {
    return "cbrne";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public IlocInstruction constantFold(Vector<Integer> constVals) {
    Integer lOpVal = (Integer) constVals.elementAt(0);

    if (lOpVal.intValue() == 0)
      return new JumpIInstruction((LabelOperand) dest);
    else
      return new NopInstruction();
  }

  @Override
  public boolean isExpression() {
    return false;
  }

  @Override
  public boolean isNecessary() {
    return false;
  }

  @Override
  public IlocInstruction deepCopy() {
    CbrneInstruction inst = new CbrneInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(CbrneInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
