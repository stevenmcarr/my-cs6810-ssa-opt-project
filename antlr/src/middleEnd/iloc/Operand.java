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
public abstract class Operand {

  protected int operandType;

  public static final int INTEGER_TYPE = 0;
  public static final int FLOAT_TYPE = 1;
  public static final int CHARACTER_TYPE = 2;

  protected void setType(int type) {
    operandType = type;
  }

  public int getType() {
    return operandType;
  }

}
