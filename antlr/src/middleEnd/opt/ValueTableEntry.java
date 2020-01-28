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
public abstract class ValueTableEntry {

  int valueNumber;

  public abstract ValueTableEntry copy();

  public int getValueNumber() {
    return valueNumber;
  }

}
