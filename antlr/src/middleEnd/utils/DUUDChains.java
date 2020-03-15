package middleEnd.utils;

import java.util.Iterator;

import middleEnd.dfa.ReachingDefinitions;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;

public class DUUDChains {
    private Cfg cfg;
    ReachingDefinitions rd;
    private DefinitionSet allDefinitions;

    public DUUDChains() {
    }

    public DUUDChains addCfg(Cfg cfg) {
        this.cfg = cfg;
        return this;
    }

    public void build() {
        rd = new ReachingDefinitions(cfg);
        rd.computeDFResult(cfg);
        BasicBlockDFMap inMap = rd.getInMap();
        for (CfgNode n : cfg.getNodes()) {
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

    private void clearCurrentDefs(DefinitionSet ds, VirtualRegisterOperand vr) {
        IlocInstructionSet iis = ds.getInstructionSet(vr.toString());
        for (int index = iis.nextSetBit(0); index >= 0; index = iis.nextSetBit(index + 1)) {
            IlocInstruction inst = iis.getIlocInstruction(index);
            ds.clear(vr.toString(), inst);
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

    public DefinitionSet getAllDefintions() {
        if (rd == null)
            return null;
        else if (allDefinitions == null) {
            allDefinitions = new DefinitionSet();
            for (String vr : rd.getDefMap().keySet()) {
                IlocInstructionSet iis = rd.getDefMap().get(vr);
                for (int index = iis.nextSetBit(0); index >= 0; index = iis.nextSetBit(index + 1)) {
                    allDefinitions.set(vr, iis.getIlocInstruction(index));
                }
            }
            return allDefinitions;

        } else
            return allDefinitions;

    }
}
