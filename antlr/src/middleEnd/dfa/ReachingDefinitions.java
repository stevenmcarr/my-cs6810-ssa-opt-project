package middleEnd.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.DefinitionSet;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.IlocInstructionSet;

public class ReachingDefinitions extends IterativeFramework {

    private DefinitionSet emptySet;
    private DefinitionSet universe;
    private BasicBlockDFMap inMap = new BasicBlockDFMap();
    private BasicBlockDFMap outMap = new BasicBlockDFMap();
    private BasicBlockDFMap genMap = new BasicBlockDFMap();
    private BasicBlockDFMap prsvMap = new BasicBlockDFMap();
    private HashMap<String, IlocInstructionSet> defMap;

    public ReachingDefinitions(Cfg g) {
        universe = new DefinitionSet();
        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction i = bIter.next();
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    universe.set(vr.toString(), i);
                    addToDefMap(vr.toString(), i);
                }
            }
        }
        emptySet = new DefinitionSet(universe);
        emptySet.clearAll();
    }

    private void addToDefMap(String vr, IlocInstruction i) {
        if (!defMap.containsKey(vr)) {
            IlocInstructionSet iis = new IlocInstructionSet();
            iis.set(i);
            defMap.put(vr, iis);
        } else {
            defMap.get(vr).set(i);
        }
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            DefinitionSet in = emptySet.clone();
            DefinitionSet gen = emptySet.clone();
            DefinitionSet prsv = universe.clone();
            ListIterator<IlocInstruction> bIter = b.reverseIterator();
            while (bIter.hasPrevious()) {
                IlocInstruction i = bIter.previous();
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    if (prsv.get(vr.toString(), i))
                        gen.set(vr.toString(), i);
                    IlocInstructionSet defInstSet = universe.getInstructionSet(vr.toString());
                    for (int j = defInstSet.nextSetBit(0); j >= 0; j = defInstSet.nextSetBit(j + 1)) {
                        IlocInstruction inst = defInstSet.getIlocInstruction(j);
                        for (VirtualRegisterOperand vro : inst.getAllLValues())
                            prsv.clear(vro.toString(), inst);
                    }
                }
            }
            inMap.put(b, in);
            genMap.put(b, gen);
            prsvMap.put(b, prsv);
            setTransferResult(b, applyTransferFunc(b));
        }
    }

    @Override
    public DataFlowSet meet(BasicBlock n) {
        DefinitionSet result = emptySet.clone();
        for (CfgEdge e : n.getPreds()) {
            BasicBlock s = (BasicBlock) e.getPred();
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
    public DataFlowSet applyTransferFunc(BasicBlock n) {
        DefinitionSet result = emptySet.clone();
        result.or(inMap.get(n));
        result.and(prsvMap.get(n));
        result.or(genMap.get(n));
        return result;
    }

    @Override
    public void setTransferResult(BasicBlock n, DataFlowSet vrs) {
        outMap.put(n, vrs);
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

    public HashMap<String, IlocInstructionSet> getDefMap() {
        return defMap;
    }

}