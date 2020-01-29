package middleEnd.utils;

import java.util.HashMap;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;

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
public class IlocRoutine {

  private Vector<BasicBlock> basicBlocks = new Vector<BasicBlock>();
  private Cfg cfg;
  private HashMap<String, IlocInstruction> labelMap = new HashMap<String, IlocInstruction>();

  public IlocRoutine() {
    super();
  }

  public Cfg getCfg() {
    return cfg;
  }

  public void addBlock(BasicBlock block) {
    basicBlocks.add(block);
  }

  public void addLabelMap(HashMap<String, IlocInstruction> lm) {
    labelMap = lm;
  }

  public Vector<BasicBlock> getBasicBlocks() {
    return basicBlocks;
  }

  public void buildCfg() {
    cfg = new Cfg();

    BasicBlock entry = basicBlocks.firstElement();
    cfg.addEntryNode(entry);

    BasicBlock exit = new BasicBlock();
    cfg.addExitNode(exit);

    Vector<BasicBlock> work = new Vector<BasicBlock>(basicBlocks);

    while (!work.isEmpty()) {
      BasicBlock b = work.firstElement();
      work.remove(b);
      IlocInstruction last = b.getLastInst();
      if (last.isBranchInstruction()) {
        BasicBlock target = labelMap.get(last.getBranchTargetLabel()).getBlock();
        b.addSuccessorEdge(b, target);
        target.addPredecessorEdge(b, target);
      }
      if (!last.isUnconditionalBranchInstruction()) {
        BasicBlock target = last.getNextInst().getBlock();
        b.addSuccessorEdge(b, target);
        target.addPredecessorEdge(b, target);
      }
    }
  }
}
