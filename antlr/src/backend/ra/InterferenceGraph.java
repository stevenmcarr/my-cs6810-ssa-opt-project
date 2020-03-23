package backend.ra;

import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;
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

public class InterferenceGraph {

    private LinkedList<InterferenceNode> nodes = new LinkedList<InterferenceNode>();
    private LinkedList<LiveRangeOperand> lrs = new LinkedList<LiveRangeOperand>();
    private LinkedList<InterferenceNode> uncoloredNodes = new LinkedList<InterferenceNode>();

    public InterferenceGraph() {}

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

	public boolean colorChaitinBriggs(HashMap<Integer,Integer> spillCost,int k) {
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
            colorNodeIfPossible(v,k);
        }

        spillUncoloredNodes();

        return uncoloredNodes.isEmpty();
            
    }

	private void spillUncoloredNodes() {
        for (InterferenceNode n : uncoloredNodes) {
            int spillLocation = n.getReferenceInstructions().getFirst().getBlock().getIlocRoutine().getSpillLocation();
            VirtualRegisterOperand svr = new VirtualRegisterOperand(VirtualRegisterOperand.maxVirtualRegister+1);
            for (IlocInstruction inst : n.getReferenceInstructions()) {
                BasicBlock b = inst.getBlock();
                Vector<Operand> operands = inst.getRValues();
                boolean spillInserted = false;
                for (int index = 0; index < operands.size(); index++) {
                    Operand op = operands.elementAt(index);
                    if (op instanceof LiveRangeOperand && 
                        ((LiveRangeOperand)op).getLiveRangeId() == n.getLR().getLiveRangeId()) {
                            inst.replaceOperandAtIndex(index,new VirtualRegisterOperand(svr.getRegisterId()));
                            if (!spillInserted) {
                                LoadAIInstruction spillInst = 
                                    new LoadAIInstruction(new VirtualRegisterOperand(VirtualRegisterOperand.FLOAT_RET_REG),
                                                      new ConstantOperand(spillLocation),
                                                      new VirtualRegisterOperand(((VirtualRegisterOperand)op).getRegisterId()));
                                b.insertBefore(inst, spillInst);
                                spillInserted = true;
                            }
                        }
                }
                int j = 0;
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof LiveRangeOperand && 
                        ((LiveRangeOperand)vr).getLiveRangeId() == n.getLR().getLiveRangeId()) {
                        StoreAIInstruction spillInst =
                            new StoreAIInstruction(new VirtualRegisterOperand(svr.getRegisterId()), 
                                                   new ConstantOperand(spillLocation), 
                                                   new VirtualRegisterOperand(VirtualRegisterOperand.FP_REG));
                        b.insertAfter(inst, spillInst);
                        inst.replaceLValue(svr, j);
                        break;
                    } 
                    j++;
                }
            }

        }
    }

    private void colorNodeIfPossible(InterferenceNode v, int k) {
        BitSet colors = new BitSet(k);
        for (InterferenceNode n : v.getAdjacentNodes()) {
            if (nodes.contains(n) && n.getColor() >= 0) {
                colors.set(n.getColor());
            }
        }
        int color = colors.nextClearBit(0);
        v.setColor(color);
        if (color < 0)
            uncoloredNodes.add(v);
    }

    private void incrementallyRebuildGraph(InterferenceNode v) {
        nodes.add(v);
        for (InterferenceNode n : v.getAdjacentNodes()) {
            if (nodes.contains(n)) {
                n.addAdjacentNode(v);
            }
        }
    }

    private InterferenceNode nodeWithLowestSpillCost(HashMap<Integer, Integer> spillCost) {
        Iterator<InterferenceNode> iter = nodes.iterator();
        InterferenceNode n = null;
        if (iter.hasNext()) {
            n = iter.next();
            Integer lowSC = spillCost.get(n.getLR().getLiveRangeId());
            while (iter.hasNext()) {
                InterferenceNode in = iter.next();
                Integer sc = spillCost.get(in.getLR().getLiveRangeId());
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
            }
            else if (n.getLR().getRegisterId() == VirtualRegisterOperand.SP_REG) {
                n.setColor(VirtualRegisterOperand.SP_REG);
                lIter.remove();
            }
            else if (n.getLR().getRegisterId() == VirtualRegisterOperand.INT_RET_REG) {
                n.setColor(VirtualRegisterOperand.INT_RET_REG);
                lIter.remove();
            }
            else if (n.getLR().getRegisterId() == VirtualRegisterOperand.FLOAT_RET_REG) {
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
            System.out.print("Node "+n.getLR().toString()+": ");
            for (InterferenceNode m : n.getAdjacentNodes()) {
                System.out.print(m.getLR().toString()+", ");
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

}