package middleEnd.opt;

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
public class RegisterValueTableEntry extends ValueTableEntry {

  int virtualReg;

  public RegisterValueTableEntry(int valueNumber, int virtualReg) {
    this.valueNumber = valueNumber;
    this.virtualReg = virtualReg;
  }

  public ValueTableEntry copy() {
    RegisterValueTableEntry newEntry = new RegisterValueTableEntry(valueNumber, virtualReg);
    return newEntry;
  }

  public int getVirtualRegister() {
    return virtualReg;
  }
}
