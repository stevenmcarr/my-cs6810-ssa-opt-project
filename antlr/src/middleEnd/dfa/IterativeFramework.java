package middleEnd.dfa;

import java.util.List;

import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.VirtualRegisterSet;

public abstract class IterativeFramework {

    public abstract void initialize(Cfg g);

    public abstract VirtualRegisterSet meet(BasicBlock n);

    public abstract VirtualRegisterSet getCurrentMeetResult(BasicBlock n);

    public abstract void setMeetResult(BasicBlock n, VirtualRegisterSet vrs);

    public abstract VirtualRegisterSet applyTransferFunc(BasicBlock n);

    public abstract void setTransferResult(BasicBlock n, VirtualRegisterSet vrs);

    public abstract List<CfgNode> getNodeOrder(Cfg g);

    public void computeDFResult(Cfg g) {
        initialize(g);

        boolean changed;
        do {
            changed = false;
            for (CfgNode n : getNodeOrder(g)) {
                BasicBlock b = (BasicBlock) n;
                VirtualRegisterSet meetResult = meet(b);
                if (!meetResult.equals(getCurrentMeetResult(b))) {
                    changed = true;
                    setMeetResult(b, meetResult);
                    VirtualRegisterSet transferResult = applyTransferFunc(b);
                    setTransferResult(b, transferResult);
                }
            }

        } while (changed);
    }
}