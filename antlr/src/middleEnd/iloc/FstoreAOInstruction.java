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
public class FstoreAOInstruction extends ThreeAddressIlocInstruction {
  public FstoreAOInstruction(VirtualRegisterOperand source1, VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    rValues.add(dest);
    rValues.add(source1);
    rValues.add(source2);
  }

  public FstoreAOInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "fstoreAO";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source1.toString() + " => " + source2.toString() + ", " + dest.toString());
    return rep;
  }

  protected int getOperandType(Operand operand) {
    if (source1 == operand)
      return Operand.FLOAT_TYPE;
    else
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
    FstoreAOInstruction inst = new FstoreAOInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FstoreAOInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
