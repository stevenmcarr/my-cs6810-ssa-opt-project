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
  private HashMap<String, IlocInstruction> labelMap = null;

  public IlocRoutine() {
    super();
  }

  public Cfg getCfg() {
    return cfg;
  }

  public void addBlock(BasicBlock block) {
    basicBlocks.add(block);
  }

  public void setLabelMap(HashMap<String, IlocInstruction> lm) {
    labelMap = lm;
  }

  public Vector<BasicBlock> getBasicBlocks() {
    return basicBlocks;
  }

  public void buildCfg() {
    cfg = new Cfg();

    Vector<BasicBlock> work = new Vector<BasicBlock>(basicBlocks);

    BasicBlock entry = new BasicBlock();
    cfg.addEntryNode(entry);

    BasicBlock target = basicBlocks.firstElement();
    entry.addSuccessorEdge(target);
    target.addPredecessorEdge(entry);

    BasicBlock exit = new BasicBlock();
    cfg.addExitNode(exit);

    while (!work.isEmpty()) {
      BasicBlock b = work.firstElement();
      work.remove(b);
      cfg.addNode(b);

      IlocInstruction last = b.getLastInst();

      if (last.isBranchInstruction()) {
        if (!last.isEndInstruction())
          target = labelMap.get(last.getBranchTargetLabel()).getBlock();
        else
          target = exit;
        b.addSuccessorEdge(target);
        target.addPredecessorEdge(b);
      }

      if (!last.isUnconditionalBranchInstruction()) {
        target = last.getNextInst().getBlock();
        //
        // Because the instrucgtions are in one large list, the next instruction may be
        // in a different routine. Make sure we are still in the same routine
        // Otherwise, the target block is the exit block.
        //
        if (basicBlocks.contains(target)) {
          b.addSuccessorEdge(target);
          target.addPredecessorEdge(b);
        }
      }
    }
  }
}
