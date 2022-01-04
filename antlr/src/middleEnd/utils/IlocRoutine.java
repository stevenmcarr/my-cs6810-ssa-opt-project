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

  private FramePseudoOp frameOp = null;

  public IlocRoutine() {
  }

  public void emitCode(PrintWriter pw) {
    for (IlocInstruction inst : instructions)
      inst.emit(pw);
  }

  protected LinkedList<IlocInstruction> getInstructions() {
    return instructions;
  }

  public IlocRoutine addInstruction(IlocInstruction inst) {
    instructions.add(inst);
    return this;
  }

  public IlocRoutine addInstruction(int index, IlocInstruction inst) {
    instructions.add(index, inst);
    return this;
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

  public String getRoutineName() {
    if (frameOp != null) {
      return frameOp.getName();
    } else
      return "";
  }
}
