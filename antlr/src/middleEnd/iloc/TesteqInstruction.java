package middleEnd.iloc;

/**
 * <p>Title: Nolife Compiler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Steve Carr
 * @version 1.0
 */
public class TesteqInstruction extends TestInstruction {

  public TesteqInstruction(VirtualRegisterOperand source,
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
    return "testeq";
  }

  public static String getHash(VirtualRegisterOperand src) {
    return "testeq"+src.toString();
  }

  public TestInstruction genComplementInst() {
    return new TestneInstruction(((VirtualRegisterOperand)source).copy(),
                                 ((VirtualRegisterOperand)dest).copy());
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }
}
