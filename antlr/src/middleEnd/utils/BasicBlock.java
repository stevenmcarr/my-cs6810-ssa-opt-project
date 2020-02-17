package middleEnd.utils;

import java.io.PrintWriter;
import java.util.*;
import middleEnd.iloc.*;

/**
 * <p>
 * Title: CS4131 Nolife Compiler
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
 * @author not attributable
 * @version 1.0
 */
public class BasicBlock extends CfgNode {

  LinkedList<IlocInstruction> instructions = new LinkedList<IlocInstruction>();
  List<PhiNode> phiNodes = new ArrayList<PhiNode>();
  IlocRoutine routine = null;

  public BasicBlock() {
  }

  public BasicBlock addRoutine(IlocRoutine rtn) {
    routine = rtn;
    return this;
  }

  public Iterator<IlocInstruction> iterator() {
    return instructions.iterator();
  }

  public ListIterator<IlocInstruction> listIterator() {
    return instructions.listIterator();
  }

  public ListIterator<IlocInstruction> reverseIterator() {
    return instructions.listIterator(instructions.size());
  }

  public void addInstruction(IlocInstruction inst) {
    instructions.add(inst);
    inst.setBlock(this);
  }

  public void insertBefore(IlocInstruction inst, IlocInstruction newInst) {
    routine.insertBefore(inst, newInst);
    int index = instructions.indexOf(inst);
    instructions.add(index, newInst);
    newInst.setBlock(this);
  }

  public void insertAfter(IlocInstruction inst, IlocInstruction newInst) {
    routine.insertAfter(inst, newInst);
    int index = instructions.indexOf(inst);
    instructions.add(index + 1, newInst);
    newInst.setBlock(this);
  }

  public void replaceInst(IlocInstruction inst, IlocInstruction newInst) {
    routine.replace(inst, newInst);
    int index = instructions.indexOf(inst);
    instructions.set(index, newInst);
    inst.setBlock(null);
    newInst.setBlock(this);
  }

  public void replaceWithIterator(ListIterator<IlocInstruction> iter, IlocInstruction oldI, IlocInstruction newI) {
    if (oldI != newI) {
      routine.replace(oldI, newI);
      oldI.setBlock(null);
      newI.setBlock(this);
      iter.set(newI);
    }
  }

  public void removeInst(IlocInstruction inst) {
    routine.remove(inst);
    instructions.remove(inst);
    inst.setBlock(null);
  }

  public void removeWithIterator(ListIterator<IlocInstruction> iter, IlocInstruction inst) {
    iter.remove();
    routine.remove(inst);
    inst.setBlock(null);
  }

  public IlocInstruction getFirstInst() {
    return instructions.getFirst();
  }

  public IlocInstruction getLastInst() {
    return instructions.getLast();
  }

  @Override
  public String getCfgNodeLabel() {
    String label = "Node " + Integer.toString(nodeId);

    Iterator<IlocInstruction> bIter = iterator();
    while (bIter.hasNext()) {
      IlocInstruction inst = bIter.next();
      label += "\n" + inst.getStringRep();
    }

    return label;
  }

  public BasicBlock addPhiNode(PhiNode n) {
    phiNodes.add(n);
    return this;
  }

  public List<PhiNode> getPhiNodes() {
    return phiNodes;
  }

  public IlocInstruction getNextInst(IlocInstruction instPtr) {
    int index = instructions.indexOf(instPtr) + 1;
    if (index >= instructions.size())
      return null;
    else
      return instructions.get(index);
  }

  public IlocInstruction getPreviousInst(IlocInstruction instPtr) {
    int index = instructions.indexOf(instPtr) - 1;
    if (index < 0)
      return null;
    else
      return instructions.get(index);
  }

  public void emitCodeWithSSA(PrintWriter pw) {
    if (!phiNodes.isEmpty()) {
      pw.println("\n\tPhi Nodes for Block " + nodeId);
      for (PhiNode p : phiNodes)
        pw.println("\t" + p.getStringRep());
      pw.println("");
    }

    emitCode(pw);
  }

  private void emitCode(PrintWriter pw) {
    for (IlocInstruction inst : instructions)
      inst.emit(pw);
  }

}
