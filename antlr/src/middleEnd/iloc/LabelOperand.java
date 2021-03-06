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
}
