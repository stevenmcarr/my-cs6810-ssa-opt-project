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
public class LabelOperand extends ImmediateOperand {

  private String label;

  public LabelOperand(String label) {
    this.label = label;
  }

  public LabelOperand() {
  }

  public String getLabel() {
    return label;
  }

  public String toString() {
    return label;
  }

  private static int newLabelId = 0;

  public static LabelOperand makeLabelOperand() {
    return new LabelOperand("GL" + newLabelId++);
  }

  @Override
  public Operand deepCopy() {
    LabelOperand lo = new LabelOperand();
    copyInstanceVars(lo);
    return lo;
  }

  public void copyInstanceVars(LabelOperand copy) {
    copy.label = new String(label);
    super.copyInstanceVars(copy);
  }
}
