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
public class ConditionRegisterOperand extends Operand {

  private int registerId;

  public ConditionRegisterOperand(int registerId) {
    this.registerId = registerId;
  }

  public int getRegisterId() {
    return registerId;
  }
}
