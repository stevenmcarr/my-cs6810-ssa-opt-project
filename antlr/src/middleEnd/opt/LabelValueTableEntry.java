package middleEnd.opt;

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
public class LabelValueTableEntry extends ValueTableEntry {
  String label;

  public LabelValueTableEntry(int valueNumber,
                              String label) {
    this.valueNumber = valueNumber;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  /**
   * copy
   *
   * @return ValueTableEntry
   * @todo Implement this middleEnd.opt.ValueTableEntry method
   */
  public ValueTableEntry copy() {
    LabelValueTableEntry newEntry = new LabelValueTableEntry(valueNumber,new String(label));
    return newEntry;
  }
}
