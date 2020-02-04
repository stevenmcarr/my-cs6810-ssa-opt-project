package middleEnd.dbre;

import middleEnd.utils.OptimizationPass;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import middleEnd.dfa.LiveVariableAnalysis;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.PhiNode;
import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockIterator;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.CfgNodeSet;
import middleEnd.utils.DominatorTreeEdge;
import middleEnd.utils.IlocRoutine;

public class DominatorBasedRedundancyElimination extends OptimizationPass {

    private HashMap<String, CfgNodeSet> iteratedDFMap = new HashMap<String, CfgNodeSet>();
    private HashMap<Integer, String> defSetMap = new HashMap<Integer, String>();
    private LiveVariableAnalysis lva;
    private HashMap<String, LinkedList<SSAVROperand>> nameStack;
    private int lastName[];
    private HashMap<String, IlocInstruction> definition;
    private HashMap<String, LinkedList<IlocInstruction>> uses;
    private AvailTableStack availTabStack;

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        prevPassAbbrev = prevPassA;
        passAbbrev = passA;
    }

    private CfgNodeSet computeSv(Cfg cfg, int i) {
        CfgNodeSet s_v = new CfgNodeSet(cfg.getNodeMap());

        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            BasicBlockIterator bIter = new BasicBlockIterator(b);
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                VirtualRegisterOperand lval = inst.getLValue();
                if (lval != null && lval.getRegisterId() == i) {
                    s_v.set(n);
                    break;
                }
            }
        }
        return s_v;
    }

    private void computeIDF(Cfg cfg) {

        for (int i = 0; i <= VirtualRegisterOperand.maxVirtualRegister; i++) {
            CfgNodeSet idf_v = new CfgNodeSet(cfg.getNodeMap());
            CfgNodeSet s_v = computeSv(cfg, i);
            if (!iteratedDFMap.containsKey(s_v.toString())) {
                LinkedList<CfgNode> work = new LinkedList<CfgNode>();
                for (int j = s_v.nextSetBit(0); j >= 0; j = s_v.nextSetBit(j + 1)) {
                    CfgNode b = s_v.getCfgNode(j);
                    work.add(b);
                }
                while (!work.isEmpty()) {
                    CfgNode b = work.removeFirst();
                    CfgNodeSet df = b.getDominanceFrontier();
                    for (int k = df.nextSetBit(0); k >= 0; k = df.nextSetBit(k + 1)) {
                        CfgNode c = df.getCfgNode(k);
                        if (!idf_v.get(c)) {
                            idf_v.set(c);
                            work.add(c);
                        }
                    }
                }
                iteratedDFMap.put(s_v.toString(), idf_v);
            }
            defSetMap.put(i, s_v.toString());
        }
    }

    private void insertPhiNodes(Cfg g) {

        for (int i = 0; i <= maxVirtualRegister; i++) {
            CfgNodeSet idf = iteratedDFMap.get(defSetMap.get(i));
            for (int j = idf.nextSetBit(0); j >= 0; j = idf.nextSetBit(j + 1)) {
                BasicBlock b = (BasicBlock) idf.getCfgNode(j);
                if (lva.getInMap().get(b.getNodeId()).get(i)) {
                    int n = b.getPreds().size();
                    Vector<Operand> operands = new Vector<Operand>(n);
                    for (int k = 0; k < n; k++)
                        operands.add(new SSAVROperand(i));
                    PhiNode p = new PhiNode(operands, new SSAVROperand(i));
                    b.addPhiNode(p);
                }
            }
        }
    }

    private LinkedList<SSAVROperand> getNameStackList(String key) {
        LinkedList<SSAVROperand> list;
        if ((list = nameStack.get(key)) == null) {
            list = new LinkedList<SSAVROperand>();
            nameStack.put(key, list);
        }
        return list;
    }

    private LinkedList<SSAVROperand> getNameStackList(int id) {
        SSAVROperand t = new SSAVROperand(id);
        return getNameStackList(t.toString());
    }

    private LinkedList<IlocInstruction> getUseList(String key) {
        LinkedList<IlocInstruction> list;
        if ((list = uses.get(key)) == null) {
            list = new LinkedList<IlocInstruction>();
            uses.put(key, list);
        }
        return list;
    }

    private LinkedList<IlocInstruction> getUseList(int id) {
        SSAVROperand t = new SSAVROperand(id);
        return getUseList(t.toString());
    }

    private SSAVROperand newName(int index) {
        lastName[index]++;
        SSAVROperand t = new SSAVROperand(index, lastName[index]);

        return t;
    }

    private void optRename(BasicBlock b) {
        LinkedList<IlocInstruction> dead = new LinkedList<IlocInstruction>();
        for (PhiNode p : b.getPhiNodes()) {
            SSAVROperand t0 = (SSAVROperand) p.getLValue();
            int index = t0.getRegisterId();
            SSAVROperand t = newName(index);
            getNameStackList(t.toString()).push(t);
            definition.put(t.toString(), p);
        }

        availTabStack.startBlock();

        BasicBlockIterator bIter = new BasicBlockIterator(b);
        while (bIter.hasNext()) {
            IlocInstruction inst = bIter.next();
            Vector<Operand> operands = inst.getRValues();
            for (int i = 0; i < operands.size(); i++) {
                Operand op = operands.elementAt(0);
                if (op instanceof VirtualRegisterOperand) {
                    VirtualRegisterOperand vr = (VirtualRegisterOperand) op;
                    inst.replaceOperandAtIndex(i, getNameStackList(vr.getRegisterId()).getFirst().copy());
                    getUseList(vr.getRegisterId()).add(inst);
                }
            }

            SSAVROperand svr = null;
            if ((svr = availTabStack.get(inst.toString())) != null) {
                SSAVROperand t = new SSAVROperand(inst.getLValue().getRegisterId());
                getNameStackList(t.toString()).push(svr);
                dead.add(inst);

            } else {
                VirtualRegisterOperand t = inst.getLValue();
                if (t != null) {
                    int index = t.getRegisterId();
                    SSAVROperand tn = newName(index);
                    getNameStackList(tn.toString()).push(tn);
                    definition.put(tn.toString(), inst);
                    availTabStack.put(inst.toString(), tn);
                }
            }

        }
        int j = 0;
        for (CfgEdge e : b.getSuccs()) {
            BasicBlock s = (BasicBlock) e.getSucc();
            for (PhiNode p : s.getPhiNodes()) {
                VirtualRegisterOperand tj = (VirtualRegisterOperand) p.getRValues().elementAt(j);
                p.replaceOperandAtIndex(j, getNameStackList(tj.getRegisterId()).getFirst().copy());
                getUseList(tj.getRegisterId()).add(p);
            }
        }

        for (DominatorTreeEdge e : b.getDTChildren()) {
            BasicBlock c = (BasicBlock) e.getSucc();
            optRename(c);
        }

        bIter = new BasicBlockIterator(b, true);
        while (bIter.hasPrevious()) {
            IlocInstruction inst = bIter.previous();
            VirtualRegisterOperand vr = inst.getLValue();
            if (vr != null) {
                SSAVROperand x = getNameStackList(vr.getRegisterId()).pop().copy();
                if (dead.contains(inst))
                    bIter.remove();
                else
                    inst.replaceLValue(x);
            }
        }

        for (PhiNode p : b.getPhiNodes()) {
            VirtualRegisterOperand vr = p.getLValue();
            p.replaceLValue(getNameStackList(vr.getRegisterId()).pop().copy());
        }

        availTabStack.endBlock();
    }

    private void rename(Cfg g) {
        nameStack = new HashMap<String, LinkedList<SSAVROperand>>();
        definition = new HashMap<String, IlocInstruction>();
        uses = new HashMap<String, LinkedList<IlocInstruction>>();
        lastName = new int[maxVirtualRegister];
        availTabStack = new AvailTableStack();
        for (int i = 0; i <= maxVirtualRegister; i++) {
            lastName[i] = -1;
        }
        optRename((BasicBlock) g.getEntryNode());
    }

    private void removeSSA(Cfg g) {
        List<CfgNode> nl = g.getNodes();

        for (CfgNode n : nl) {
            BasicBlock b = (BasicBlock) n;
            BasicBlockIterator bIter = new BasicBlockIterator(b);
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                Vector<Operand> operands = inst.getRValues();
                for (int i = 0; i < operands.size(); i++) {
                    Operand op = operands.elementAt(i);
                    if (op instanceof SSAVROperand) {
                        VirtualRegisterOperand vr = new VirtualRegisterOperand(((SSAVROperand) op).getRegisterId());
                        inst.replaceOperandAtIndex(i, vr);
                    }
                }

                VirtualRegisterOperand lval = inst.getLValue();
                if (lval != null && lval instanceof SSAVROperand) {
                    VirtualRegisterOperand vr = new VirtualRegisterOperand(((SSAVROperand) lval).getRegisterId());
                    inst.replaceLValue(vr);
                }
            }
        }
    }

    @Override
    protected void optimizeCode() {

        PrintWriter pw = null;
        PrintWriter pw2 = null;
        try {
            pw = new PrintWriter("graph.dot");
            pw2 = new PrintWriter("df");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (IlocRoutine ir : getRoutines()) {
            ir.buildCfg();
            if (driver.CodeGenerator.emitCfg)
                ir.getCfg().emitCfg(pw);
            ir.getCfg().buildPreOrder();
            ir.getCfg().buildPostOrder();
            ir.getCfg().buildDT();
            if (driver.CodeGenerator.emitDT)
                ir.getCfg().emitDT(pw);
            ir.getCfg().buildDF();
            computeIDF(ir.getCfg());
            if (driver.CodeGenerator.emitDF) {
                ir.getCfg().emitDF(pw2);
                emitIteratedDF(pw2);
            }
            lva = new LiveVariableAnalysis(maxVirtualRegister);
            lva.computeDFResult(ir.getCfg());
            insertPhiNodes(ir.getCfg());
            rename(ir.getCfg());
            removeSSA(ir.getCfg());
        }
        pw.close();
        pw2.close();
    }

    private void emitIteratedDF(PrintWriter pw) {
        pw.println("Iterated Dominance Frontiers");
        pw.println("----------------------------\n");

        pw.println("\tDef Set Map");
        pw.println("\t-----------");
        Set<Integer> defKeys = defSetMap.keySet();
        Iterator<Integer> dki = defKeys.iterator();
        while (dki.hasNext()) {
            int i = dki.next();
            pw.println("\t" + i + " -> " + defSetMap.get(i).toString());
            dki.remove();
        }
        pw.println("");

        pw.println("\tIterated DF Map");
        pw.println("\t---------------");
        Set<String> idfKeys = iteratedDFMap.keySet();
        Iterator<String> idfki = idfKeys.iterator();
        while (idfki.hasNext()) {
            String s = idfki.next();
            pw.println("\t" + s + " -> " + iteratedDFMap.get(s).toString());
            idfki.remove();
        }

        pw.println("");
    }

}