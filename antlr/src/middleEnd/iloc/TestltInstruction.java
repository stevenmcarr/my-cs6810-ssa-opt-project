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
public class TestltInstruction extends TestInstruction {
  public TestltInstruction(VirtualRegisterOperand source,
      VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  public TestltInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "testlt";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "testlt" + src.toString();
  }

  public TestInstruction genComplementInst() {
    return new TestgeInstruction(((VirtualRegisterOperand) source).copy(),
        ((VirtualRegisterOperand) dest).copy());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    TestltInstruction inst = new TestltInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(TestltInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
