package middleEnd.iloc;

/**
 * <p>
 * Title: CS4131 Nolife Compiler
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
 * @author not attributable
 * @version 1.0
 */
public class TestgeInstruction extends TestInstruction {
  public TestgeInstruction(VirtualRegisterOperand source,
      VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  public TestgeInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "testge";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "testge" + src.toString();
  }

  public TestInstruction genComplementInst() {
    return new TestltInstruction(((VirtualRegisterOperand) source).copy(),
        ((VirtualRegisterOperand) dest).copy());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    TestgeInstruction inst = new TestgeInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(TestgeInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
