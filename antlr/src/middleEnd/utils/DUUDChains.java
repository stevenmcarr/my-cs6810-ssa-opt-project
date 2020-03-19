package middleEnd.utils;

import java.util.Iterator;
import java.util.Vector;

import middleEnd.dfa.ReachingDefinitions;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.LiveRangeOperand;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;

public class DUUDChains {
    private Cfg cfg;
    ReachingDefinitions rd;

    public DUUDChains() {
    }

    public DUUDChains addCfg(Cfg cfg) {
        this.cfg = cfg;
        return this;
    }

    public void build() {
        rd = new ReachingDefinitions(cfg);
        rd.computeDFResult(cfg);
        emitRD(rd);
        BasicBlockDFMap inMap = rd.getInMap();
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock) n;
            DefinitionSet ds = (DefinitionSet) inMap.get(b);
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction i = bIter.next();
                for (Operand op : i.getRValues()) {
                    if (op instanceof VirtualRegisterOperand) {
                        IlocInstructionSet iis = ds.getInstructionSet(op.toString());
                        linkDefsToUse(iis, i, (VirtualRegisterOperand) op);
                    }
                }
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    clearCurrentDefs(ds, vr);
                    ds.set(vr.toString(), i);
                }
            }
        }
    }

    private void emitRD(ReachingDefinitions rd) {
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock)n;
            System.out.println("Basic Block :" + b.getNodeId());

            if (rd.getInMap().get(b) != null)
                System.out.println("\tin  =  " + rd.getInMap().get(b).toString());
            else
                System.out.println("\tin  =  EmptySet");
            if (rd.getGenMap().get(b) != null)
                System.out.println("\tgen = " + rd.getGenMap().get(b).toString());
            else
                System.out.println("\tgen = EmptySet");
            if (rd.getPrsvMap().get(b) != null)
                System.out.println("\tprsv = " + rd.getPrsvMap().get(b).toString());
            else
                System.out.println("\tprsv = EmptySet");
            if (rd.getOutMap().get(b) != null)
                System.out.println("\tout = " + rd.getOutMap().get(b).toString() + "\n\n");
            else
                System.out.println("\tout = EmptySet");
        }
    }

    private void clearCurrentDefs(DefinitionSet ds, VirtualRegisterOperand vr) {
        IlocInstructionSet iis = ds.getInstructionSet(vr.toString());
        if (iis != null) {
            for (int index = iis.nextSetBit(0); index >= 0; index = iis.nextSetBit(index + 1)) {
                IlocInstruction inst = iis.getIlocInstruction(index);
                ds.clear(vr.toString(), inst);
            }
        }
    }

    private void linkDefsToUse(IlocInstructionSet iis, IlocInstruction thisInst, VirtualRegisterOperand op) {
        for (int index = iis.nextSetBit(0); index >= 0; index = iis.nextSetBit(index + 1)) {
            IlocInstruction inst = iis.getIlocInstruction(index);
            op.addDef(inst);
            for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                vr.addUse(thisInst);
            }

        }
    }

	public void rename() {
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock)n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues()) {
                    if (op instanceof VirtualRegisterOperand) {
                        LiveRangeOperand lro = new LiveRangeOperand((VirtualRegisterOperand)op);
                        renameLiveRange((VirtualRegisterOperand)op,lro);
                    }
                }
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (!(vr instanceof LiveRangeOperand)) {
                        LiveRangeOperand lro = new LiveRangeOperand(vr);
                        renameLiveRange(vr,lro);
                    }
                }
            }
        }
	}

    private void renameLiveRange(VirtualRegisterOperand op, LiveRangeOperand lro) {
        for (IlocInstruction inst : op.getDefs())
            replaceOperand(inst,op,lro);
        for (IlocInstruction inst : op.getUses())
            replaceOperand(inst,op,lro);
    }

    private void replaceOperand(IlocInstruction inst, VirtualRegisterOperand vr, LiveRangeOperand lro) {
        Vector<Operand> operands = inst.getRValues();
        for (int i = 0; i < operands.size(); i++) {
            Operand op = operands.elementAt(i);
            if (!(op instanceof LiveRangeOperand) && vr.toString().equals(op.toString())) {
                inst.replaceOperandAtIndex(i,lro);
            }
        }
        int j = 0;
        for (VirtualRegisterOperand vro : inst.getAllLValues())
            if (!(vro instanceof LiveRangeOperand) && vr.toString().equals(vro.toString())) {
                inst.replaceLValue(lro, j);
            }
            j++;
    }


	public void resetLiveRanges() {
        for (CfgNode n : cfg.getPostOrder()) {
            BasicBlock b = (BasicBlock)n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> operands = inst.getRValues();
                for (int index = 0; index < operands.size(); index++) {
                    Operand op = operands.elementAt(index);
                    if (op instanceof LiveRangeOperand) {
                        VirtualRegisterOperand vr = new VirtualRegisterOperand(((VirtualRegisterOperand)op).getRegisterId());
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
}
