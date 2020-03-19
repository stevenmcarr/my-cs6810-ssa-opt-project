package backend.ra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import middleEnd.dfa.LiveVariableAnalysis;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.LiveRangeOperand;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DUUDChains;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;
import middleEnd.utils.VirtualRegisterSet;

public class ChaitinBriggs extends OptimizationPass {

    private InterferenceGraph ig = new InterferenceGraph();
    private HashMap<Integer,Integer> spillCost = new HashMap<Integer,Integer>();

    public ChaitinBriggs(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {
        for (IlocRoutine ir : getRoutines()) {
            ir.buildCfg();
            ir.getCfg().buildPreOrder();
            ir.getCfg().buildPostOrder();
            ir.getCfg().buildDT(); 
            ir.buildInstructionMap();
            do {
                DUUDChains chains = (new DUUDChains()).addCfg(ir.getCfg());
                chains.resetLiveRanges();
                chains.build();
                chains.rename();
                LiveVariableAnalysis lva = new LiveVariableAnalysis(VirtualRegisterOperand.maxVirtualRegister);
                lva.computeDFResult(ir.getCfg());
                buildInterferenceGraph(ir.getCfg(), lva);
                computeSpillCosts(ir.getCfg());
            } while (!ig.colorChaitinBriggs(spillCost,16));

            assignColors();
        }
    }

    private void assignColors() {
        for (InterferenceNode n : ig.getNodes()) {
            for (IlocInstruction inst : n.getReferenceInstructions()) {
                 Vector<Operand> operands = inst.getRValues();
                for (int index = 0; index < operands.size(); index++) {
                    Operand op = operands.elementAt(index);
                    if (op instanceof LiveRangeOperand) {
                        VirtualRegisterOperand vr = new VirtualRegisterOperand(n.getColor());
                        inst.replaceOperandAtIndex(index, vr);
                    }
                }
                int j = 0;
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof LiveRangeOperand) {
                        VirtualRegisterOperand vrn = new VirtualRegisterOperand(n.getColor());
                        inst.replaceLValue(vrn, j);
                    }
                    j++;
                }
            }
        }
    }

    private void buildInterferenceGraph(Cfg cfg, LiveVariableAnalysis lva) {
        BasicBlockDFMap outMap = lva.getOutMap();
        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock)n;
            if (outMap.get(b) != null) {
                VirtualRegisterSet live = (VirtualRegisterSet)outMap.get(b).clone();
                ListIterator<IlocInstruction> rIter = b.reverseIterator();
                while (rIter.hasPrevious()) {
                    IlocInstruction inst = rIter.previous();
                    for (Operand op : inst.getAllLValues()) {
                        if (op instanceof LiveRangeOperand) {
                            LiveRangeOperand lr = (LiveRangeOperand)op;
                            InterferenceNode lrNode = ig.addNode(lr);
                            lrNode.addInst(inst);
                            for (int i = live.nextSetBit(0); i >= 0; i = live.nextSetBit(i)) {
                                LiveRangeOperand lr2 = new LiveRangeOperand(i);
                                InterferenceNode lr2Node = ig.addNode(lr2); 
                                lrNode.addAdjacentNode(lr2Node);
                                lr2Node.addAdjacentNode(lrNode);
                            }
                            live.clear(lr);
                        }
                    }
                    for (Operand op : inst.getRValues()) {
                        if (op instanceof LiveRangeOperand)
                            live.set((LiveRangeOperand)op);
                    }
                }
            }
        }
    }

    private void computeSpillCosts(Cfg g) {
        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock)n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues()) {
                    if (op instanceof LiveRangeOperand) {
                        LiveRangeOperand lr = (LiveRangeOperand)op;
                        addSpillCost(lr,getSpillCost(b,lr));
                    }
                }
            }
        }
    }

    private void addSpillCost(LiveRangeOperand lr, Integer nsc) {
        Integer sc = spillCost.get(lr.getLiveRangeId());
        if (sc == null) {
            spillCost.put(lr.getLiveRangeId(),nsc);
        } else {
            sc = sc + nsc;
        }
    }

    private Integer getSpillCost(BasicBlock b, LiveRangeOperand lr) {
        int depth = b.getLoopNode().getNestingDepth();
        return 3 * pow10(depth); 
    }

    private int pow10(int depth) {
        int val = 1;
        for (int i = 0; i < depth; i++)
            val *= 10;

        return val;
    }
}