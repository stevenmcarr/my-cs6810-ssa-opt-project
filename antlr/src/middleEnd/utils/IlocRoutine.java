package middleEnd.utils;

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
public class IlocRoutine {

  Vector<BasicBlock> basicBlocks = new Vector<BasicBlock>();

  public IlocRoutine() {
    super();
  }

  public void addBlock(BasicBlock block) {
    basicBlocks.add(block);
  }

  public Vector<BasicBlock> getBasicBlocks() {
    return basicBlocks;
  }
}
