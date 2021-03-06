package middleEnd.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import middleEnd.iloc.FramePseudoOp;
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

  private LinkedList<IlocInstruction> instructions = new LinkedList<IlocInstruction>();
  private Vector<BasicBlock> basicBlocks = new Vector<BasicBlock>();
  private Cfg cfg;
  private HashMap<String, IlocInstruction> labelMap = null;
  private HashMap<String, IlocInstruction> definitionMap = null;
  private HashMap<Integer, IlocInstruction> instMap = null;
  private int frameSize = 0;
  private FramePseudoOp frameOp= null;

  public IlocRoutine() {
  }

  public void emitCode(PrintWriter pw) {
    for (IlocInstruction inst : instructions)
      inst.emit(pw);
  }

  protected LinkedList<IlocInstruction> getInstructions() {
    return instructions;
  }

  public void buildBasicBlocks() {
    ListIterator<IlocInstruction> lIter = instructions.listIterator();
    if (lIter.hasNext()) {
      IlocInstruction inst = lIter.next();

      BasicBlock block = (new BasicBlock()).addRoutine(this);
      basicBlocks.add(block);
      block.addInstruction(inst);

      if (inst instanceof FramePseudoOp) {
        frameOp = (FramePseudoOp)inst;
        frameSize = frameOp.getLocalSize();
      }

      IlocInstruction prevInst = inst;
      while (lIter.hasNext()) {
        inst = lIter.next();
        if (inst.getLabel() != null || prevInst.isBranchInstruction()) {
          block = (new BasicBlock()).addRoutine(this);
          basicBlocks.add(block);
        }
        block.addInstruction(inst);
        prevInst = inst;
      }

    }

  }

  public Cfg getCfg() {
    return cfg;
  }

  public void addBlock(BasicBlock block) {
    basicBlocks.add(block);
    block.addRoutine(this);
  }

  public IlocRoutine addInstruction(IlocInstruction inst) {
    instructions.add(inst);
    return this;
  }

  public IlocRoutine addInstruction(int index, IlocInstruction inst) {
    instructions.add(index, inst);
    return this;
  }

  public void setLabelMap(HashMap<String, IlocInstruction> lm) {
    labelMap = lm;
  }

  public void setDefintionMap(HashMap<String, IlocInstruction> dm) {
    definitionMap = dm;
  }

  public HashMap<String, IlocInstruction> getDefinitionMap() {
    return definitionMap;
  }

  public Vector<BasicBlock> getBasicBlocks() {
    return basicBlocks;
  }

  public void buildCfg() {
    buildBasicBlocks();
    cfg = new Cfg();

    Vector<BasicBlock> work = new Vector<BasicBlock>(basicBlocks);

    BasicBlock entry = (new BasicBlock()).addRoutine(this);
    cfg.addEntryNode(entry);

    BasicBlock target = basicBlocks.firstElement();
    entry.addSuccessorEdge(target);
    target.addPredecessorEdge(entry);

    BasicBlock exit = (new BasicBlock()).addRoutine(this);
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
        int index = instructions.indexOf(last) + 1;
        if (index < instructions.size()) {
          target = instructions.get(index).getBlock();
          //
          // Because the instructions are in one large list, the next instruction may be
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

  public void insertBefore(IlocInstruction inst, IlocInstruction newInst) {
    int instIndex = instructions.indexOf(inst);
    instructions.add(instIndex, newInst);
  }

  public void insertAfter(IlocInstruction inst, IlocInstruction newInst) {
    int instIndex = instructions.indexOf(inst);
    instructions.add(instIndex + 1, newInst);
  }

  public void replace(IlocInstruction inst, IlocInstruction newInst) {
    instructions.set(instructions.indexOf(inst), newInst);
  }

  public void remove(IlocInstruction inst) {
    instructions.remove(inst);
  }

  public boolean isEmpty() {
    return instructions.isEmpty();
  }

  public int getInstructionIndex(IlocInstruction inst) {
    return instructions.indexOf(inst);
  }

  public IlocInstruction getFirstInst() {
    return instructions.getFirst();
  }

  public IlocInstruction getLastInst() {
    return instructions.getLast();
  }

  public IlocInstruction getInstructionAtIndex(int index) {
    return instructions.get(index);
  }

  public IlocInstruction getNextInstruction(IlocInstruction inst) {
    int index = getInstructionIndex(inst);
    if (index < 0 || index >= instructions.size() - 1)
      return null;
    else
      return instructions.get(index + 1);
  }

  public void emitCodeWithSSA(PrintWriter pw) {
    for (BasicBlock b : basicBlocks)
      b.emitCodeWithSSA(pw);
  }

  public void setInstructionMap(HashMap<Integer, IlocInstruction> instMap) {
    this.instMap = instMap;
  }

  public HashMap<Integer, IlocInstruction> getInstructionMap() {
    return instMap;
  }

  public int getSpillLocation() {
    frameSize += 4;
    frameOp.updateFrameSize(frameSize);
	  return frameSize-4;
  }

  public void buildInstructionMap() {
        HashMap<Integer, IlocInstruction> instMap = new HashMap<Integer, IlocInstruction>();
        for (BasicBlock b : getBasicBlocks()) {
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                instMap.put(inst.getInstId(), inst);
        }

        setInstructionMap(instMap);
    }
  }

  public String getRoutineName() {
    if (frameOp != null) {
      return frameOp.getName();
    } else
      return "";
  }
}
