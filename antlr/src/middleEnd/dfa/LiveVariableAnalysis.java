package middleEnd.dfa;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.VirtualRegisterSet;

public class LiveVariableAnalysis extends IterativeFramework {

    private VirtualRegisterSet emptySet;
    private VirtualRegisterSet universe;
    private HashMap<Integer, VirtualRegisterSet> inMap;
    private HashMap<Integer, VirtualRegisterSet> outMap;
    private HashMap<Integer, VirtualRegisterSet> genMap;
    private HashMap<Integer, VirtualRegisterSet> prsvMap;

    public LiveVariableAnalysis(int size) {
        emptySet = new VirtualRegisterSet(size);
        universe = new VirtualRegisterSet(size);
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            outMap.put(b.getNodeId(), emptySet.clone());
            genMap.put(b.getNodeId(), emptySet.clone());
            prsvMap.put(b.getNodeId(), universe.clone());
            for (IlocInstruction inst = b.getFirstInst(); inst != null
                    && inst != b.getLastInst().getNextInst(); inst = inst.getNextInst()) {
                Vector<Operand> rv = inst.getRValues();
                for (Operand op : rv) {
                    if (op instanceof VirtualRegisterOperand
                            && prsvMap.get(b.getNodeId()).get((VirtualRegisterOperand) op)) {
                        genMap.get(b.getNodeId()).set((VirtualRegisterOperand) op);
                    }
                }
                if (inst.getLValue() != null)
                    prsvMap.get(b.getNodeId()).clear(inst.getLValue());
            }
        }
    }

    @Override
    public VirtualRegisterSet meet(BasicBlock n) {
        VirtualRegisterSet result = universe.clone();
        for (CfgEdge e : n.getSuccs()) {
            BasicBlock s = (BasicBlock) e.getSucc();
            result.or(inMap.get(s.getNodeId()));
        }
        return result;
    }

    @Override
    public VirtualRegisterSet getCurrentMeetResult(BasicBlock n) {
        return outMap.get(n.getNodeId());
    }

    @Override
    public void setMeetResult(BasicBlock n, VirtualRegisterSet vrs) {
        outMap.put(n.getNodeId(), vrs);
    }

    @Override
    public VirtualRegisterSet applyTransferFunc(BasicBlock n) {
        VirtualRegisterSet result = emptySet.clone();
        result.or(outMap.get(n.getNodeId()));
        result.and(prsvMap.get(n.getNodeId()));
        result.or(genMap.get(n.getNodeId()));
        return result;
    }

    @Override
    public void setTransferResult(BasicBlock n, VirtualRegisterSet vrs) {
        inMap.put(n.getNodeId(), vrs);
    }

    @Override
    public List<CfgNode> getNodeOrder(Cfg g) {
        return g.getPreOrder();
    }

    public HashMap<Integer, VirtualRegisterSet> getInMap() {
        return inMap;
    }

    public HashMap<Integer, VirtualRegisterSet> getOutMap() {
        return outMap;
    }

    public HashMap<Integer, VirtualRegisterSet> getGenMap() {
        return genMap;
    }

    public HashMap<Integer, VirtualRegisterSet> getPrsvMap() {
        return prsvMap;
    }

}