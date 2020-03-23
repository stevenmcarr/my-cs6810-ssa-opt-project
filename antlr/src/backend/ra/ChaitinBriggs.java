package backend.ra;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import driver.CodeGenerator;
import middleEnd.dfa.LVALiveRangeOperand;
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
import middleEnd.utils.LiveRangeSet;
import middleEnd.utils.OptimizationPass;

public class ChaitinBriggs extends OptimizationPass {

    private InterferenceGraph ig = new InterferenceGraph();
    private HashMap<Integer, Integer> spillCost = new HashMap<Integer, Integer>();

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

                LVALiveRangeOperand lva = new LVALiveRangeOperand(LiveRangeOperand.numLiveRanges);
                lva.computeDFResult(ir.getCfg());
                if (CodeGenerator.emitLVA)
                    try {
                        PrintWriter pw = new PrintWriter(inputFileName + "." + ir.getRoutineName() + ".lva");
                        emitLVA(lva, pw, ir);
                        pw.close();
                    } catch (FileNotFoundException e) {
                        System.err.println("Can't emit live variable analysis for routine " + ir.getRoutineName());
                    }
                buildInterferenceGraph(ir.getCfg(), chains.getLiveRangeOperands(), lva);
                if (CodeGenerator.emitIG)
                    try {
                        PrintWriter pw = new PrintWriter(inputFileName + "." + ir.getRoutineName() + ".ig.dot");
                        ig.emit(pw);
                        pw.close();
                    } catch (FileNotFoundException e) {
                        System.err.println("Can't emit interference graph for routine " + ir.getRoutineName());
                    }
                computeSpillCosts(ir.getCfg());
            } while (!ig.colorChaitinBriggs(spillCost, 16));

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

    private void buildInterferenceGraph(Cfg cfg, LinkedList<LiveRangeOperand> liveRangeNames, LVALiveRangeOperand lva) {
        for (LiveRangeOperand lrn : liveRangeNames) {
            ig.addNode(lrn);
        }
        BasicBlockDFMap outMap = lva.getOutMap();
        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            if (outMap.get(b) != null) {
                LiveRangeSet live = (LiveRangeSet) outMap.get(b).clone();
                ListIterator<IlocInstruction> rIter = b.reverseIterator();
                while (rIter.hasPrevious()) {
                    IlocInstruction inst = rIter.previous();
                    for (Operand op : inst.getAllLValues()) {
                        if (op instanceof LiveRangeOperand) {
                            LiveRangeOperand lr = (LiveRangeOperand) op;
                            InterferenceNode lrNode = ig.getNode(lr);
                            lrNode.addInst(inst);
                            for (int i = live.nextSetBit(0); i >= 0; i = live.nextSetBit(i + 1)) {
                                LiveRangeOperand lr2 = getLiveRangeNode(liveRangeNames, i);
                                if (lr2.getLiveRangeId() != lr.getLiveRangeId()) {
                                    InterferenceNode lr2Node = ig.getNode(lr2);
                                    lrNode.addAdjacentNode(lr2Node);
                                    lr2Node.addAdjacentNode(lrNode);
                                }
                            }
                            live.clear(lr);
                        }
                    }
                    for (Operand op : inst.getRValues()) {
                        if (op instanceof LiveRangeOperand)
                            live.set((LiveRangeOperand) op);
                    }
                }
            }
        }
    }

    private LiveRangeOperand getLiveRangeNode(LinkedList<LiveRangeOperand> liveRangeNames, int lrId) {
        LiveRangeOperand op = null;
        for (LiveRangeOperand lro : liveRangeNames) {
            if (lro.getLiveRangeId() == lrId)
                op = lro;
        }
        return op;
    }

    private void computeSpillCosts(Cfg g) {
        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues()) {
                    if (op instanceof LiveRangeOperand) {
                        LiveRangeOperand lr = (LiveRangeOperand) op;
                        addSpillCost(lr, getSpillCost(b, lr));
                    }
                }
            }
        }
    }

    private void addSpillCost(LiveRangeOperand lr, Integer nsc) {
        Integer sc = spillCost.get(lr.getLiveRangeId());
        if (sc == null) {
            spillCost.put(lr.getLiveRangeId(), nsc);
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

    private void emitLVA(LVALiveRangeOperand lva, PrintWriter pwl, IlocRoutine ir) {
        for (BasicBlock b : ir.getBasicBlocks()) {
            pwl.println("Basic Block :" + b.getNodeId());

            if (lva.getInMap().get(b) != null)
                pwl.println("\tin  =  " + lva.getInMap().get(b).toString());
            else
                pwl.println("\tin  =  EmptySet");
            if (lva.getGenMap().get(b) != null)
                pwl.println("\tgen = " + lva.getGenMap().get(b).toString());
            else
                pwl.println("\tgen = EmptySet");
            if (lva.getPrsvMap().get(b) != null)
                pwl.println("\tprsv = " + lva.getPrsvMap().get(b).toString());
            else
                pwl.println("\tprsv = EmptySet");
            if (lva.getOutMap().get(b) != null)
                pwl.println("\tout = " + lva.getOutMap().get(b).toString() + "\n\n");
            else
                pwl.println("\tout = EmptySet");
        }
    }
}