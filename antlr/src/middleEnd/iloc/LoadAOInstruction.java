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
public class LoadAOInstruction extends ThreeAddressIlocInstruction {
  public LoadAOInstruction(VirtualRegisterOperand source1, VirtualRegisterOperand source2,
      VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public LoadAOInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "loadAO";
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public boolean isExpression() {
    return false;
  }

  @Override
  public IlocInstruction deepCopy() {
    LoadAOInstruction inst = new LoadAOInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(LoadAOInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
