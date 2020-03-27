package backend.ra;

import java.io.PrintWriter;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import middleEnd.iloc.ConstantOperand;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.LiveRangeOperand;
import middleEnd.iloc.LoadAIInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.StoreAIInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;

public class InterferenceGraph {

    private LinkedList<InterferenceNode> nodes = new LinkedList<InterferenceNode>();
    private LinkedList<LiveRangeOperand> lrs = new LinkedList<LiveRangeOperand>();
    private LinkedList<InterferenceNode> uncoloredNodes = null;
    private int[] spillLocation;

    public InterferenceGraph() {
    }

    public InterferenceNode addNode(LiveRangeOperand lr) {
        InterferenceNode n;
        int index;
        if ((index = lrs.indexOf(lr)) < 0) {
            lrs.add(lr);
            n = (new InterferenceNode()).addLR(lr);
            nodes.add(n);
        } else
            n = nodes.get(index);

        return n;
    }

    public boolean colorChaitinBriggs(Cfg g, double[] spillCost, int k) {
        uncoloredNodes = new LinkedList<InterferenceNode>();
        preColor(nodes);
        LinkedList<InterferenceNode> s = new LinkedList<InterferenceNode>();
        while (!nodes.isEmpty()) {
            InterferenceNode n = nodeWithDegreeLessThanK(k);
            if (n == null)
                n = nodeWithLowestSpillCost(spillCost);
            n.removeEdges();
            nodes.remove(n);
            s.addFirst(n);
        }

        while (!s.isEmpty()) {
            InterferenceNode v = s.pop();
            incrementallyRebuildGraph(v);
            colorNodeIfPossible(v, k);
        }

        spillUncoloredNodes(g);

        return uncoloredNodes.isEmpty();

    }

    private void spillUncoloredNodes(Cfg g) {
        spillLocation = new int[LiveRangeOperand.numLiveRanges];
        for (int i = 0; i < spillLocation.length; i++)
            spillLocation[i] = -1;
        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            ListIterator<IlocInstruction> bIter = b.listIterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> operands = inst.getRValues();
                BitSet spillInserted = new BitSet();
                for (int index = 0; index < operands.size(); index++) {
                    Operand op = operands.elementAt(index);
                    if (op instanceof LiveRangeOperand) {
                        LiveRangeOperand lro = (LiveRangeOperand) op;
                        if (isUncolored(lro)) {
                            inst.replaceOperandAtIndex(index, new LiveRangeOperand(lro));
                            if (!spillInserted.get(lro.getLiveRangeId())) {
                                int stackLocation = getSpillLocation(inst, lro);
                                LoadAIInstruction spillInst = new LoadAIInstruction(
                                        new VirtualRegisterOperand(VirtualRegisterOperand.FP_REG),
                                        new ConstantOperand(-stackLocation),
                                        new LiveRangeOperand(lro));
                                if (inst.getLabel() != null) {
                                    String label = inst.getLabel();
                                    spillInst.setLabel(label);
                                    inst.setLabel(null);
                                }
                                b.insertBeforeWithIterator(bIter,inst, spillInst);
                                spillInserted.set(lro.getLiveRangeId());
                            }
                        }
                    }
                }
                int j = 0;
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof LiveRangeOperand) {
                        LiveRangeOperand lro = (LiveRangeOperand) vr;
                        if (isUncolored(lro)) {
                            StoreAIInstruction spillInst = new StoreAIInstruction(
                                    new LiveRangeOperand(lro),
                                    new ConstantOperand(-getSpillLocation(inst, lro)),
                                    new VirtualRegisterOperand(VirtualRegisterOperand.FP_REG));
                            b.insertAfterWithIterator(bIter,inst, spillInst);
                            inst.replaceLValue(new LiveRangeOperand(lro), j);
                        }
                    }
                    j++;
                }
            }

        }
    }

    private int getSpillLocation(IlocInstruction inst, LiveRangeOperand lro) {
        int loc = spillLocation[lro.getLiveRangeId()];
        if (loc < 0) {
            loc = inst.getBlock().getIlocRoutine().getSpillLocation();
            spillLocation[lro.getLiveRangeId()] = loc;
        }
        return loc;
    }

    private boolean isUncolored(LiveRangeOperand lro) {
        InterferenceNode in = null;
        for (InterferenceNode n : uncoloredNodes) {
            if (n.getLR().getLiveRangeId() == lro.getLiveRangeId()) {
                in = n;
                break;
            }
        }
        return in != null;
    }

    private void colorNodeIfPossible(InterferenceNode v, int k) {
        BitSet colors = new BitSet(k);
        for (InterferenceNode n : v.getAdjacentNodes()) {
            if (nodes.contains(n) && n.getColor() >= 0) {
                colors.set(n.getColor());
            }
        }
        int color = colors.nextClearBit(4); // 0 - 3 are reserved registers
        if (color >= k)
            uncoloredNodes.add(v);
        else
            v.setColor(color);
    }

    private void incrementallyRebuildGraph(InterferenceNode v) {
        nodes.add(v);
        for (InterferenceNode n : v.getAdjacentNodes()) {
            if (nodes.contains(n)) {
                n.addAdjacentNode(v);
            }
        }
    }

    private InterferenceNode nodeWithLowestSpillCost(double[] spillCost) {
        Iterator<InterferenceNode> iter = nodes.iterator();
        InterferenceNode n = null;
        if (iter.hasNext()) {
            n = iter.next();
            double lowSC = spillCost[n.getLR().getLiveRangeId()]/(double)n.getDegree();
            while (iter.hasNext()) {
                InterferenceNode in = iter.next();
                double sc = spillCost[in.getLR().getLiveRangeId()]/(double)in.getDegree();
                if (sc < lowSC) {
                    lowSC = sc;
                    n = in;
                }
            }
        }
        return n;
    }

    private InterferenceNode nodeWithDegreeLessThanK(int k) {
        Iterator<InterferenceNode> iter = nodes.iterator();
        InterferenceNode n = null;
        while (iter.hasNext() && n == null) {
            InterferenceNode in = iter.next();
            if (in.getDegree() < k)
                n = in;
        }
        return null;
    }

    private void preColor(LinkedList<InterferenceNode> nodes2) {
        ListIterator<InterferenceNode> lIter = nodes.listIterator();
        while (lIter.hasNext()) {
            InterferenceNode n = lIter.next();
            if (n.getLR().getRegisterId() == VirtualRegisterOperand.FP_REG) {
                n.setColor(VirtualRegisterOperand.FP_REG);
                lIter.remove();
            } else if (n.getLR().getRegisterId() == VirtualRegisterOperand.SP_REG) {
                n.setColor(VirtualRegisterOperand.SP_REG);
                lIter.remove();
            } else if (n.getLR().getRegisterId() == VirtualRegisterOperand.INT_RET_REG) {
                n.setColor(VirtualRegisterOperand.INT_RET_REG);
                lIter.remove();
            } else if (n.getLR().getRegisterId() == VirtualRegisterOperand.FLOAT_RET_REG) {
                n.setColor(VirtualRegisterOperand.FLOAT_RET_REG);
                lIter.remove();
            }
        }
    }

    public LinkedList<InterferenceNode> getNodes() {
        return nodes;
    }

    public void dump() {
        System.out.println("Interference Graph");
        System.out.println("------------------");
        for (InterferenceNode n : nodes) {
            System.out.print("Node " + n.getLR().toString() + ": ");
            for (InterferenceNode m : n.getAdjacentNodes()) {
                System.out.print(m.getLR().toString() + ", ");
            }
            System.out.println("");
        }
    }

    public InterferenceNode getNode(LiveRangeOperand lr) {
        InterferenceNode node = null;
        for (InterferenceNode n : nodes) {
            if (n.getLR().toString().equals(lr.toString())) {
                node = n;
                break;
            }
        }
        return node;
    }

    public void emit(PrintWriter pw) {

        pw.println("graph interference_graph {");

        for (InterferenceNode n : nodes) {
            pw.println(n.getNodeId() + " [ label = \"" + n.getLR() + "\", shape = circle]");
            for (InterferenceNode an : n.getAdjacentNodes()) {
                if (nodes.indexOf(n) <= nodes.indexOf(an))
                    pw.println(n.getNodeId() + "--" + an.getNodeId());
            }
        }

        pw.println("}");

    }

    public int getInterferenceNodeColor(LiveRangeOperand op) {
        InterferenceNode in = null;
        for (InterferenceNode n : nodes) {
            if (n.getLR().getLiveRangeId() == op.getLiveRangeId()) {
                in = n;
                break;
            }
        }
        if (in == null) // This is for pre-color regs who get the same color as the vr number
            return op.getRegisterId();
        else
            return in.getColor();
    }

}