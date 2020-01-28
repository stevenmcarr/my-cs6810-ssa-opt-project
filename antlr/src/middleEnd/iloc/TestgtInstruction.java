package middleEnd.iloc;

/**
 * <p>Title: CS4131 Nolife Compiler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TestgtInstruction extends TestInstruction {
  public TestgtInstruction(VirtualRegisterOperand source,
                           VirtualRegisterOperand dest) {
    this.source = source;
    this.dest = dest;
    lValue = dest;
    rValues.add(source);
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return "testgt";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "testgt"+src.toString();
  }

  public TestInstruction genComplementInst() {
    return new TestleInstruction(((VirtualRegisterOperand)source).copy(),
                                 ((VirtualRegisterOperand)dest).copy());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }
}
