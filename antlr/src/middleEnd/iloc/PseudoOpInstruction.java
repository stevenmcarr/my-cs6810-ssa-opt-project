package middleEnd.iloc;

import java.util.Hashtable;

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
public abstract class PseudoOpInstruction extends IlocInstruction {

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
  }

  public boolean operandIsLValue(Operand operand) {
    return false;
  }

  public boolean operandIsRValue(Operand operand) {
    return false;
  }
}
