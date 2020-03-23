package middleEnd.dfa;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.LiveRangeOperand;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.LiveRangeSet;

public class LVALiveRangeOperand extends IterativeFramework {

    private LiveRangeSet emptySet;
    private LiveRangeSet universe;
    private BasicBlockDFMap inMap = new BasicBlockDFMap();
    private BasicBlockDFMap outMap = new BasicBlockDFMap();
    private BasicBlockDFMap genMap = new BasicBlockDFMap();
    private BasicBlockDFMap prsvMap = new BasicBlockDFMap();

    public LVALiveRangeOperand(int size) {
        emptySet = new LiveRangeSet(size + 1);
        universe = new LiveRangeSet(size + 1);
        universe.set(0, size + 1);
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            LiveRangeSet out = emptySet.clone();
            LiveRangeSet gen = emptySet.clone();
            LiveRangeSet prsv = universe.clone();
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> rv = inst.getRValues();
                for (Operand op : rv) {
                    if (op instanceof LiveRangeOperand && prsv.get((LiveRangeOperand) op)) {
                        gen.set((LiveRangeOperand) op);
                    }
                }
                if (inst.getLValue() != null)
                    prsv.clear((LiveRangeOperand)inst.getLValue());

            }
            outMap.put(b, out);
            genMap.put(b, gen);
            prsvMap.put(b, prsv);
            setTransferResult(b, applyTransferFunc(b));
        }
    }

    @Override
    public DataFlowSet meet(BasicBlock n) {
        LiveRangeSet result = emptySet.clone();
        for (CfgEdge e : n.getSuccs()) {
            BasicBlock s = (BasicBlock) e.getSucc();
            result.or(inMap.get(s));
        }
        return result;
    }

    @Override
    public DataFlowSet getCurrentMeetResult(BasicBlock n) {
        return outMap.get(n);
    }

    @Override
    public void setMeetResult(BasicBlock n, DataFlowSet vrs) {
        outMap.put(n, vrs);
    }

    @Override
    public LiveRangeSet applyTransferFunc(BasicBlock n) {
        LiveRangeSet result = emptySet.clone();
        result.or(outMap.get(n));
        result.and(prsvMap.get(n));
        result.or(genMap.get(n));
        return result;
    }

    @Override
    public void setTransferResult(BasicBlock n, DataFlowSet vrs) {
        inMap.put(n, vrs);
    }

    @Override
    public List<CfgNode> getNodeOrder(Cfg g) {
        return g.getPostOrder();
    }

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

}