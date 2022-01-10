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
public class TestneInstruction extends TestInstruction {
  public TestneInstruction(VirtualRegisterOperand source,
      VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  public TestneInstruction() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "testne";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "testne" + src.toString();
  }

  public TestInstruction genComplementInst() {
    return new TesteqInstruction(((VirtualRegisterOperand) source).copy(),
        ((VirtualRegisterOperand) dest).copy());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  @Override
  public IlocInstruction deepCopy() {
    TestneInstruction inst = new TestneInstruction();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(TestneInstruction inst) {
    super.copyInstanceVars(inst);
  }
}
