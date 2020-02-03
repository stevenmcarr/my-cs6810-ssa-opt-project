package middleEnd.utils;

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

  IlocInstruction firstInst = null;
  IlocInstruction lastInst = null;
  List<PhiNode> phiNodes = new ArrayList<PhiNode>();

  public BasicBlock() {
  }

  public void add(IlocInstruction inst) {
    if (firstInst == null)
      firstInst = inst;
    lastInst = inst;
    inst.setBlock(this);
  }

  public void insertBefore(IlocInstruction inst, IlocInstruction newInst) {
    if (firstInst == inst)
      firstInst = newInst;
    newInst.setBlock(this);
  }

  public void insertAfter(IlocInstruction inst, IlocInstruction newInst) {
    if (lastInst == inst)
      lastInst = newInst;
    newInst.setBlock(this);
  }

  public void replaceInst(IlocInstruction inst, IlocInstruction newInst) {
    inst.setBlock(null);
    newInst.setBlock(this);

    if (firstInst == inst)
      firstInst = newInst;

    if (lastInst == inst)
      lastInst = newInst;
  }

  public void removeInst(IlocInstruction inst) {
    inst.setBlock(null);

    if (firstInst == inst)
      firstInst = inst.getNextInst();

    if (lastInst == inst)
      lastInst = inst.getPrevInst();
  }

  public IlocInstruction getFirstInst() {
    return firstInst;
  }

  public IlocInstruction getLastInst() {
    return lastInst;
  }

  @Override
  public String getCfgNodeLabel() {
    String label = "Node " + Integer.toString(nodeId);

    for (IlocInstruction inst = firstInst; inst != lastInst; inst = inst.getNextInst()) {
      label += "\n" + inst.getStringRep();
    }
    if (lastInst != null)
      label += "\n" + lastInst.getStringRep();

    return label;
  }

  public BasicBlock addPhiNode(PhiNode n) {
    phiNodes.add(n);
    return this;
  }

  public List<PhiNode> getPhiNodes() {
    return phiNodes;
  }
}
