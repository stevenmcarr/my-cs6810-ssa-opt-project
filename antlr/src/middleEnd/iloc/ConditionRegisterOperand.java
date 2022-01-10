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
public class ConditionRegisterOperand extends Operand {

  private int registerId;

  public ConditionRegisterOperand(int registerId) {
    this.registerId = registerId;
  }

  public ConditionRegisterOperand() {
  }

  public int getRegisterId() {
    return registerId;
  }

  @Override
  public Operand deepCopy() {
    ConditionRegisterOperand lo = new ConditionRegisterOperand();
    copyInstanceVars(lo);
    return lo;
  }

  public void copyInstanceVars(ConditionRegisterOperand copy) {
    copy.registerId = registerId;
    super.copyInstanceVars(copy);
  }
}
