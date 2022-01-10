package middleEnd.dfa;

import java.util.Iterator;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.VirtualRegisterSet;

public class LiveVariableAnalysis extends BackwardDataFlowProblem {

    private VirtualRegisterSet emptySet;
    private VirtualRegisterSet universe;

    public LiveVariableAnalysis(int size) {
        emptySet = new VirtualRegisterSet(size + 1);
        universe = new VirtualRegisterSet(size + 1);
        universe.set(0, size + 1);
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            VirtualRegisterSet out = emptySet.clone();
            VirtualRegisterSet gen = emptySet.clone();
            VirtualRegisterSet prsv = universe.clone();
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> rv = inst.getRValues();
                for (Operand op : rv) {
                    if (op instanceof VirtualRegisterOperand && prsv.get((VirtualRegisterOperand) op)) {
                        gen.set((VirtualRegisterOperand) op);
                    }
                }
                if (inst.getLValue() != null)
                    prsv.clear(inst.getLValue());

            }
            outMap.put(b, out);
            genMap.put(b, gen);
            prsvMap.put(b, prsv);
            setTransferResult(b, applyTransferFunc(b));
        }
    }

    @Override
    public DataFlowSet meet(BasicBlock n) {
        VirtualRegisterSet result = emptySet.clone();
        for (CfgEdge e : n.getSuccs()) {
            BasicBlock s = (BasicBlock) e.getSucc();
            result.or(inMap.get(s));
        }
        return result;
    }

    @Override
    public VirtualRegisterSet applyTransferFunc(BasicBlock n) {
        VirtualRegisterSet result = emptySet.clone();
        result.or(outMap.get(n));
        result.and(prsvMap.get(n));
        result.or(genMap.get(n));
        return result;
    }

}