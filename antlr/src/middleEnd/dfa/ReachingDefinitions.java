package middleEnd.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.VRInstPair;
import middleEnd.utils.VRInstPairSet;

public class ReachingDefinitions extends IterativeFramework {

    private Cfg g;
    private VRInstPairSet emptySet;
    private VRInstPairSet universe;
    private BasicBlockDFMap inMap = new BasicBlockDFMap();
    private BasicBlockDFMap outMap = new BasicBlockDFMap();
    private BasicBlockDFMap genMap = new BasicBlockDFMap();
    private BasicBlockDFMap prsvMap = new BasicBlockDFMap();
    private HashMap<String, LinkedList<VRInstPair>> defMap = new HashMap<String,LinkedList<VRInstPair>>();

    public ReachingDefinitions(Cfg g) {
        this.g = g;
        universe = new VRInstPairSet();
        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction i = bIter.next();
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    VRInstPair vrp = (new VRInstPair()).addVR(vr).addInst(i);
                    universe.set(vrp);
                    addToDefMap(vr, vrp);
                }
            }
        }
        emptySet = universe.clone();
        emptySet.clearAll();
    }

    private void addToDefMap(VirtualRegisterOperand vr, VRInstPair vrp) {
        String key = vr.toString();
        if (!defMap.containsKey(key)) {
            LinkedList<VRInstPair> l = new LinkedList<VRInstPair>();
            l.add(vrp);
            defMap.put(key, l);
        } else {
            defMap.get(key).add(vrp);
        }
    }

    private VRInstPair findPair(VirtualRegisterOperand vr, IlocInstruction i) {
        LinkedList<VRInstPair> l = defMap.get(vr.toString());
        VRInstPair pair = null;
        for (VRInstPair vrp : l) {
            if (vrp.isPairEqual(vr,i)) {
                pair = vrp;
                break;
            }
        }
        return pair;
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            VRInstPairSet in = emptySet.clone();
            VRInstPairSet gen = emptySet.clone();
            VRInstPairSet prsv = universe.clone();
            ListIterator<IlocInstruction> bIter = b.reverseIterator();
            while (bIter.hasPrevious()) {
                IlocInstruction i = bIter.previous();
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    VRInstPair pair = findPair(vr,i);
                    if (prsv.get(pair))
                        gen.set(pair);
                    LinkedList<VRInstPair> l = defMap.get(vr.toString());
                    for (VRInstPair vrp : l) {
                        prsv.clear(vrp);
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
        VRInstPairSet result = emptySet.clone();
        for (CfgEdge e : n.getPreds()) {
            BasicBlock s = (BasicBlock) e.getPred();
            result.or(outMap.get(s));
        }
        return result;
    }

    @Override
    public DataFlowSet getCurrentMeetResult(BasicBlock n) {
        return inMap.get(n);
    }

    @Override
    public void setMeetResult(BasicBlock n, DataFlowSet vrs) {
        inMap.put(n, vrs);
    }

    @Override
    public DataFlowSet applyTransferFunc(BasicBlock n) {
        VRInstPairSet result = emptySet.clone();
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
        return g.getPreOrder();
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

    public HashMap<String, LinkedList<VRInstPair>> getDefMap() {
        return defMap;
    }

    public void emitRD() {
        for (CfgNode n : g.getPostOrder()) {
            BasicBlock b = (BasicBlock)n;
            System.out.println("Basic Block :" + b.getNodeId());

            if (inMap.get(b) != null)
                System.out.println("\tin  =  " + inMap.get(b).toString());
            else
                System.out.println("\tin  =  EmptySet");
            if (genMap.get(b) != null)
                System.out.println("\tgen = " + genMap.get(b).toString());
            else
                System.out.println("\tgen = EmptySet");
            if (prsvMap.get(b) != null)
                System.out.println("\tprsv = " + prsvMap.get(b).toString());
            else
                System.out.println("\tprsv = EmptySet");
            if (outMap.get(b) != null)
                System.out.println("\tout = " + outMap.get(b).toString() + "\n\n");
            else
                System.out.println("\tout = EmptySet");
        }
    }

}