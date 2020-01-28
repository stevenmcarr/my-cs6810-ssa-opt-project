package middleEnd.iloc;

import java.util.Vector;

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
public abstract class InvocationInstruction extends
        VariableAddressIlocInstruction {

  private static int counter = 0;

  /**
   * Each call site gets a unique hash key.
   *
   * @param valueNums Vector
   * @return String
   */
  public String getHashKey(Vector<Integer> valueNums) {
    String hashKey = getOpcode() + counter++;
    for (int i = 0; i < valueNums.size(); i++)
      hashKey += ("&" + ((Integer)valueNums.elementAt(i)).toString());

    return hashKey;
  }
}
