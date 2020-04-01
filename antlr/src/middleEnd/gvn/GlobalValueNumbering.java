package middleEnd.gvn;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.PhiNode;
import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.SSAOptimization;

public class GlobalValueNumbering extends SSAOptimization {

    private SSAGraph ssaGraph;
    HashMap<String, SSAVROperand> valueTable = null;
    HashMap<String, SSAVROperand> scratchTable = null;
    HashMap<String, SSAVROperand> valRep = null;

    public GlobalValueNumbering(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void performSSAOptimization() {
        for (IlocRoutine ir : getRoutines()) {
            buildSSAGraph(ir.getCfg());
            try {
                PrintWriter pw = new PrintWriter(inputFileName + "." + ir.getRoutineName() + ".ssag.dot");
                ssaGraph.emit(pw);
                pw.close();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            ssaGraph.computeSCCTarjanAlgorithm();
            try {
                PrintWriter pw = new PrintWriter(inputFileName+"."+ir.getRoutineName()+".scc");
                for (StronglyConnectedComponent scc : ssaGraph.getSCCs()) {
                    pw.print(scc.getSCCId()+": ");
                    for (SSAGraphNode n : scc) {
                        pw.print(n.getSSAVR().toString()+", ");
                    }
                    pw.println("");
                }
                pw.close();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            valueTable = new HashMap<String, SSAVROperand>();
            scratchTable = new HashMap<String, SSAVROperand>();
            valRep = new HashMap<String, SSAVROperand>();
            for (String t : ssaGraph.defMap.keySet()) {
                valRep.put(t, null);
            }
            for (StronglyConnectedComponent scc : ssaGraph.getSCCs()) {
                if (scc.size() > 1) {
                    calcGlobalValueSCC(scc);
                    for (SSAGraphNode n : scc) {
                        SSAVROperand svr = n.getSSAVR();
                        IlocInstruction inst = ssaGraph.getSSAGraphNodeDef(svr.toString()).getInst();
                        SSAVROperand u = valRep.get(svr.toString());
                        String key = getHashKey(inst);
                        if (!valueTable.containsKey(key)) {
                            valueTable.put(key, u);
                        }
                    }
                } else {
                    IlocInstruction inst = ssaGraph.getNodes().getFirst().getInst();
                    if (inst instanceof PhiNode)
                        calcPhiValue((PhiNode) inst, valueTable);
                    else {
                        for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                            if (vr instanceof SSAVROperand) {
                                SSAVROperand t = (SSAVROperand) vr;
                                String key = getHashKey(inst);
                                if (!valueTable.containsKey(key)) {
                                    valRep.put(t.toString(), t);
                                    valueTable.put(key, t);
                                } else {
                                    valRep.put(key, valueTable.get(key));
                                }
                            }
                        }
                    }
                }

            }
            renameSVRs(ir.getCfg());
        }
    }

    private void renameSVRs(Cfg cfg) {
    }

    private SSAVROperand calcPhiValue(PhiNode p, HashMap<String, SSAVROperand> valueTable) {
        String key = getHashKey(p);
        SSAVROperand newValRep = null;
        if (!valueTable.containsKey(key)) {
            newValRep = getPhiNodeValRep(p.getRValues());
            if (newValRep == null)
                newValRep = (SSAVROperand) p.getLValue();
            valueTable.put(key, newValRep);
        } else
            newValRep = valueTable.get(key);

        return newValRep;
    }

    private SSAVROperand getPhiNodeValRep(Vector<Operand> rValues) {
        SSAVROperand val = null;
        for (Operand op : rValues) {
            if (op instanceof SSAVROperand) {
                SSAVROperand svr = (SSAVROperand) op;
                if (val == null)
                    val = svr;
                else if (!svr.toString().equals(val.toString())) {
                    val = null;
                    break;
                }
            }
        }
        return val;
    }

    private String getHashKey(IlocInstruction inst) {
        String key = inst.getOpcode();
        for (Operand op : inst.getRValues()) {
            if (op instanceof SSAVROperand) {
                SSAVROperand svr = (SSAVROperand) op;
                SSAVROperand val = valRep.get(svr.toString());
                if (val == null)
                    key += "null";
                else
                    key += val.toString();
            } else
                key += op.toString();
        }
        return key;
    }

    private void calcGlobalValueSCC(StronglyConnectedComponent scc) {
        boolean change = false;
        do {
            SSAVROperand newValue = null;
            for (SSAGraphNode n : scc) {
                SSAVROperand t = n.getSSAVR();
                IlocInstruction i = ssaGraph.getSSAGraphNodeDef(t.toString()).getInst();
                if (i instanceof PhiNode) {
                    newValue = calcPhiValue((PhiNode) i, scratchTable);
                } else {
                    String key = getHashKey(i);
                    if (scratchTable.containsKey(key))
                        newValue = scratchTable.get(key);
                    else {
                        newValue = t;
                        scratchTable.put(key, t);
                    }
                }
                if (newValue.toString().equals(getValRepString(t))) {
                    change = true;
                    valRep.put(t.toString(), newValue);
                }
            }
        } while (change);
    }

    private void buildSSAGraph(Cfg cfg) {
        ssaGraph = new SSAGraph();
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            for (PhiNode p : b.getPhiNodes())
                buildSSAGraphNodes(p);
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                buildSSAGraphNodes(inst);
            }
        }
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            for (PhiNode p : b.getPhiNodes())
                buildSSAGraph(p);
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                buildSSAGraph(inst);
            }
        }
    }

    private void buildSSAGraphNodes(IlocInstruction inst) {
        for (VirtualRegisterOperand vr : inst.getAllLValues()) {
            if (vr instanceof SSAVROperand) {
                SSAVROperand lval = (SSAVROperand) vr;
                SSAGraphNode lnode = ssaGraph.getSSAGraphNodeDef(lval.toString());
                if (lnode == null) {
                    lnode = (new SSAGraphNode()).addInst(inst).addSSAVR(lval);
                    ssaGraph.addNode(lnode);
                    ssaGraph.addDef(lval.toString(), lnode);
                }
            }
        }
    }

    private void buildSSAGraph(IlocInstruction inst) {
        for (Operand op : inst.getRValues()) {
            if (op instanceof SSAVROperand) {
                SSAVROperand rval = (SSAVROperand) op;
                SSAGraphNode rnode = (new SSAGraphNode()).addInst(inst).addSSAVR(rval);
                ssaGraph.addNode(rnode);
                SSAGraphNode lnode = ssaGraph.getSSAGraphNodeDef(rval.toString());
                lnode.addAdjacentNode(rnode);
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof SSAVROperand) {
                        SSAVROperand lval = (SSAVROperand) vr;
                        SSAGraphNode lnode2 = ssaGraph.getSSAGraphNodeDef(lval.toString());
                        rnode.addAdjacentNode(lnode2);
                    }
                }
            }
        }
    }

    public String getValRepString(SSAVROperand t) {
        SSAVROperand val = valRep.get(t.toString());
        if (val == null)
            return "null";
        else
            return val.toString();
    }
}