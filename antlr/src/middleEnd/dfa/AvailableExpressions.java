package middleEnd.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.VirtualRegisterSet;

public class AvailableExpressions extends IterativeFramework {

    private Cfg g;
    private VirtualRegisterSet emptySet;
    private VirtualRegisterSet universe;
    private BasicBlockDFMap inMap = new BasicBlockDFMap();
    private BasicBlockDFMap outMap = new BasicBlockDFMap();
    private BasicBlockDFMap genMap = new BasicBlockDFMap();
    private BasicBlockDFMap prsvMap = new BasicBlockDFMap();
    private HashMap<String, VirtualRegisterSet> appearsMap = new HashMap<String, VirtualRegisterSet>();

    public AvailableExpressions(Cfg g) {
        this.g = g;
        universe = new VirtualRegisterSet(VirtualRegisterOperand.maxVirtualRegister + 1);
        universe.set(0, VirtualRegisterOperand.maxVirtualRegister + 1);
        emptySet = new VirtualRegisterSet(VirtualRegisterOperand.maxVirtualRegister + 1);
        emptySet.clear(0, VirtualRegisterOperand.maxVirtualRegister + 1);

        for (CfgNode n : g.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction i = bIter.next();
                for (Operand op : i.getRValues()) {
                    if (op instanceof VirtualRegisterOperand) {
                        VirtualRegisterSet appears = getAppearsSet((VirtualRegisterOperand) op);
                        for (VirtualRegisterOperand vr : i.getAllLValues()) {
                            appears.set(vr);
                        }
                    }
                }
            }
        }
    }

    public VirtualRegisterSet getAppearsSet(VirtualRegisterOperand op) {
        VirtualRegisterSet appears = appearsMap.get(op.toString());
        if (appears == null) {
            appears = new VirtualRegisterSet(VirtualRegisterOperand.maxVirtualRegister + 1);
            appearsMap.put(op.toString(), appears);
        }
        return appears;
    }

    @Override
    public void initialize(Cfg g) {
        for (CfgNode n : getNodeOrder(g)) {
            BasicBlock b = (BasicBlock) n;
            VirtualRegisterSet in = null;
            if (n == g.getEntryNode())
                in = emptySet.clone();
            else
                in = universe.clone();
            VirtualRegisterSet gen = emptySet.clone();
            VirtualRegisterSet prsv = universe.clone();
            VirtualRegisterSet killed = emptySet.clone();
            ListIterator<IlocInstruction> bIter = b.reverseIterator();
            while (bIter.hasPrevious()) {
                IlocInstruction i = bIter.previous();
                boolean opKilled = false;
                for (Operand op : i.getRValues()) {
                    if (op instanceof VirtualRegisterOperand && killed.get((VirtualRegisterOperand) op)) {
                        opKilled = true;
                        break;
                    }
                }
                for (VirtualRegisterOperand vr : i.getAllLValues()) {
                    if (!opKilled)
                        gen.set(vr);
                    killed.set(vr);
                    VirtualRegisterSet appears = getAppearsSet(vr);
                    for (int index = appears.nextSetBit(0); index >= 0; index = appears.nextSetBit(index + 1))
                        prsv.clear(index);
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
        VirtualRegisterSet result;
        if (n.getPreds().isEmpty())
            result = emptySet.clone();
        else {
            result = universe.clone();
            for (CfgEdge e : n.getPreds()) {
                BasicBlock p = (BasicBlock) e.getPred();
                result.and(outMap.get(p));
            }
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
        VirtualRegisterSet result = emptySet.clone();
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

    public void emitAE() {
        for (CfgNode n : g.getPostOrder()) {
            BasicBlock b = (BasicBlock) n;
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