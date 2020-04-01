package middleEnd.gcse;

import java.util.ListIterator;

import middleEnd.dfa.AvailableExpressions;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;
import middleEnd.utils.VirtualRegisterSet;

public class GCSE extends OptimizationPass {

    public GCSE(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {
        for (IlocRoutine ir : getRoutines()) {
            ir.buildCfg();
            ir.getCfg().buildPreOrder();
            AvailableExpressions ae = new AvailableExpressions(ir.getCfg());
            ae.computeDFResult(ir.getCfg());
            eliminateReduancy(ir.getCfg(), ae);
        }

    }

    private void eliminateReduancy(Cfg cfg, AvailableExpressions ae) {
        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            VirtualRegisterSet avail = (VirtualRegisterSet) ae.getInMap().get(b);
            ListIterator<IlocInstruction> iter = b.listIterator();
            while (iter.hasNext()) {
                IlocInstruction i = iter.next();
                boolean allInAvail = true;
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    if (!avail.get(vr)) {
                        allInAvail = false;
                        break;
                    }
                }
                if (allInAvail && i.isExpression())
                    b.removeWithIterator(iter, i);
                else {
                    for (VirtualRegisterOperand vr : i.getAllLValues()) {
                        avail.set(vr);
                        VirtualRegisterSet appears = ae.getAppearsSet(vr);
                        for (int index = appears.nextSetBit(0); index >= 0; index = appears.nextSetBit(index + 1))
                            avail.clear(index);
                    }
                }
            }
        }
    }

}