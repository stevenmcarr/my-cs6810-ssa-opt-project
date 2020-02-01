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
public class VirtualRegisterOperand extends Operand {

  public static final int FP_REG = 0;
  public static final int SP_REG = 1;
  public static final int INT_RET_REG = 2;
  public static final int FLOAT_RET_REG = 3;
  public static final int FREE_REG = 4;
  public static int maxVirtualRegister = 4;

  private int registerId;

  public VirtualRegisterOperand(int registerId) {
    this.registerId = registerId;
    if (registerId > maxVirtualRegister)
      maxVirtualRegister = registerId;
  }

  public VirtualRegisterOperand copy() {
    return new VirtualRegisterOperand(registerId);
  }

  public int getRegisterId() {
    return registerId;
  }

  public String toString() {
    return "%vr" + registerId;
  }
}
