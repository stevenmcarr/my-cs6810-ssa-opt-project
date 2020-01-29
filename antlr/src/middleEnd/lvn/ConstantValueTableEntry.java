package middleEnd.lvn;

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
public class ConstantValueTableEntry extends ValueTableEntry {
  int constantVal;

  public ConstantValueTableEntry(int valueNum, int constantVal) {
    this.valueNumber = valueNum;
    this.constantVal = constantVal;
  }

  /**
   * copy
   *
   * @return ValueTableEntry
   * @todo Implement this middleEnd.opt.ValueTableEntry method
   */
  public ValueTableEntry copy() {
    ConstantValueTableEntry newEntry = new ConstantValueTableEntry(valueNumber, constantVal);
    return newEntry;
  }

  public int getConstantValue() {
    return constantVal;
  }
}
