package middleEnd.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import middleEnd.dfa.ReachingDefinitions;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.LiveRangeOperand;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;

public class DUUDChains {
    private Cfg cfg;
    private ReachingDefinitions rd;
    private HashMap<String, LinkedList<VRInstPair>> defMap;
    private LinkedList<LinkedList<VRInstPair>> liveRanges = new LinkedList<LinkedList<VRInstPair>>();
    private LinkedList<LiveRangeOperand> liveRangeOperands = new LinkedList<LiveRangeOperand>();

    public DUUDChains() {
    }

    public DUUDChains addCfg(Cfg cfg) {
        this.cfg = cfg;
        return this;
    }

    public void build() {
        rd = new ReachingDefinitions(cfg);
        rd.computeDFResult(cfg);
        defMap = rd.getDefMap();
        BasicBlockDFMap inMap = rd.getInMap();
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock) n;
            VRInstPairSet ps = (VRInstPairSet) inMap.get(b);
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction i = bIter.next();
                for (Operand op : i.getRValues()) {
                    if (op instanceof VirtualRegisterOperand) {
                        linkDefsToUse(ps, i, (VirtualRegisterOperand) op);
                    }
                }
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    clearCurrentDefs(ps, vr);
                    ps.set(findPair(vr, i));
                }
            }
        }
    }

    private VRInstPair findPair(VirtualRegisterOperand vr, IlocInstruction i) {
        LinkedList<VRInstPair> pl = defMap.get(vr.toString());
        VRInstPair vpr = null;
        for (VRInstPair pair : pl) {
            if (pair.isPairEqual(vr, i)) {
                vpr = pair;
                break;
            }
        }
        return vpr;
    }

    private void clearCurrentDefs(VRInstPairSet ps, VirtualRegisterOperand vr) {
        LinkedList<VRInstPair> pl = defMap.get(vr.toString());
        for (VRInstPair pair : pl) {
            ps.clear(pair);
        }
    }

    private void linkDefsToUse(VRInstPairSet ps, IlocInstruction thisInst, VirtualRegisterOperand op) {
        for (VRInstPair pair : defMap.get(op.toString())) {
            if (ps.get(pair)) {
                IlocInstruction inst = pair.getInst();
                op.addDef(inst);
                for (VirtualRegisterOperand vr : inst.getAllLValues())
                    if (op.sameVR(vr))
                        vr.addUse(thisInst);
            }
        }
    }

    public void rename() {
        clearVisited();
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues()) {
                    if (op instanceof VirtualRegisterOperand && !((VirtualRegisterOperand) op).hasBeenVisited()) {
                        buildLiveRange((VirtualRegisterOperand) op, inst);
                    }
                }
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (!vr.hasBeenVisited()) {
                        buildLiveRange(vr, inst);
                    }
                }
            }
        }
        for (LinkedList<VRInstPair> pairL : liveRanges) {
            renameLiveRange(pairL);
        }
    }

    private void renameLiveRange(LinkedList<VRInstPair> pairL) {
        Iterator<VRInstPair> plIter = pairL.iterator();
        LiveRangeOperand lro = null;
        if (plIter.hasNext()) {
            VRInstPair pair = plIter.next();
            lro = new LiveRangeOperand(pair.getVR());
            liveRangeOperands.add(lro); 
            replaceOperand(pair.getInst(), pair.getVR(), lro);
            while (plIter.hasNext()) {
                pair = plIter.next();
                replaceOperand(pair.getInst(),pair.getVR(),new LiveRangeOperand(lro));
            }
        }
    }

    private void clearVisited() {
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues())
                    if (op instanceof VirtualRegisterOperand)
                        ((VirtualRegisterOperand) op).clearVisited();
                for (VirtualRegisterOperand vr : inst.getAllLValues())
                    vr.clearVisited();
            }
        }
    }

    private void buildLiveRange(VirtualRegisterOperand vr, IlocInstruction inst) {
        LinkedList<VRInstPair> pairL = new LinkedList<VRInstPair>();
        pairL.add((new VRInstPair()).addVR(vr).addInst(inst));
        buildLiveRange(vr, pairL);
        liveRanges.add(pairL);
    }

    private void printLiveRange(LinkedList<VRInstPair> pairL) {
        System.out.println("Live range");
        System.out.println("----------");
        for (VRInstPair pair : pairL) {
            System.out.println("\t" + pair.getVR().toString() + ", " + pair.getInst().getStringRep());
        }
        System.out.println("");
    }

    private void buildLiveRange(VirtualRegisterOperand vr, LinkedList<VRInstPair> pairL) {
        vr.setVisited();
        for (IlocInstruction inst : vr.getUses()) {
            for (Operand op : inst.getRValues()) {
                if (op instanceof VirtualRegisterOperand) {
                    VirtualRegisterOperand vr2 = (VirtualRegisterOperand) op;
                    if (!vr2.hasBeenVisited() && vr.sameVR(vr2)) {
                        buildLiveRange(vr2, pairL);
                        pairL.add((new VRInstPair()).addVR(vr2).addInst(inst));
                    }
                }
            }
        }
        for (IlocInstruction inst : vr.getDefs()) {
            for (Operand op : inst.getAllLValues()) {
                if (op instanceof VirtualRegisterOperand) {
                    VirtualRegisterOperand vr2 = (VirtualRegisterOperand) op;
                    if (!vr2.hasBeenVisited() && vr.sameVR(vr2)) {
                        buildLiveRange(vr2, pairL);
                        pairL.add((new VRInstPair()).addVR(vr2).addInst(inst));
                    }
                }
            }
        }
    }

    private void replaceOperand(IlocInstruction inst, VirtualRegisterOperand vr, LiveRangeOperand lro) {
        boolean found = false;
        Vector<Operand> operands = inst.getRValues();
        for (int i = 0; i < operands.size() && !found; i++) {
            Operand op = operands.elementAt(i);
            if (op instanceof VirtualRegisterOperand) {
                VirtualRegisterOperand vrop = (VirtualRegisterOperand) op;
                if (vrop.equals(vr)) {
                    inst.replaceOperandAtIndex(i, lro);
                    found = true;
                }
            }
        }
        if (!found) {
            Vector<VirtualRegisterOperand> lValues = inst.getAllLValues();
            for (int j = 0; j < lValues.size() && !found; j++) {
                VirtualRegisterOperand vrop = lValues.elementAt(j);
                if (vrop.equals(vr)) {
                    inst.replaceLValue(lro, j);
                    found = true;
                }
            }
        }
    }

    public void resetLiveRanges() {
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> operands = inst.getRValues();
                for (int index = 0; index < operands.size(); index++) {
                    Operand op = operands.elementAt(index);
                    if (op instanceof LiveRangeOperand) {
                        VirtualRegisterOperand vr = new VirtualRegisterOperand(
                                ((VirtualRegisterOperand) op).getRegisterId());
                        inst.replaceOperandAtIndex(index, vr);
                    }
                }
                int j = 0;
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof LiveRangeOperand) {
                        VirtualRegisterOperand vrn = new VirtualRegisterOperand(vr.getRegisterId());
                        inst.replaceLValue(vrn, j);
                    }
                    j++;
                }
            }
        }
    }

    public LinkedList<LiveRangeOperand> getLiveRangeOperands() {
        return liveRangeOperands;
    }
}