package middleEnd.dfa;

import java.util.List;

import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;

public abstract class IterativeFramework {

    protected BasicBlockDFMap inMap = new BasicBlockDFMap();
    protected BasicBlockDFMap outMap = new BasicBlockDFMap();
    protected BasicBlockDFMap genMap = new BasicBlockDFMap();
    protected BasicBlockDFMap prsvMap = new BasicBlockDFMap();

    public BasicBlockDFMap getInMap() {
        return inMap;
    }

    public BasicBlockDFMap getOutMap() {
        return outMap;
    }

    public BasicBlockDFMap getGenMap() {
        return genMap;
    }

    public BasicBlockDFMap getPrsvMap() {
        return prsvMap;
    }

    public abstract void initialize(Cfg g);

    public abstract DataFlowSet meet(BasicBlock n);

    public abstract DataFlowSet getCurrentMeetResult(BasicBlock n);

    public abstract void setMeetResult(BasicBlock n, DataFlowSet vrs);

    public abstract DataFlowSet applyTransferFunc(BasicBlock n);

    public abstract void setTransferResult(BasicBlock n, DataFlowSet vrs);

    public abstract List<CfgNode> getNodeOrder(Cfg g);

    public void computeDFResult(Cfg g) {
        initialize(g);

        boolean changed;
        do {
            changed = false;
            for (CfgNode n : getNodeOrder(g)) {
                BasicBlock b = (BasicBlock) n;
                DataFlowSet meetResult = meet(b);
                if (!meetResult.equals(getCurrentMeetResult(b))) {
                    changed = true;
                    setMeetResult(b, meetResult);
                    DataFlowSet transferResult = applyTransferFunc(b);
                    setTransferResult(b, transferResult);
                }
            }

        } while (changed);
    }
}