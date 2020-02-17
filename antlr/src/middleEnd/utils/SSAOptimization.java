package middleEnd.utils;

import middleEnd.utils.VirtualRegisterSet;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import middleEnd.dfa.LiveVariableAnalysis;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.PhiNode;
import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.CfgNodeSet;
import middleEnd.utils.DominatorTreeEdge;
import middleEnd.utils.IlocRoutine;

public abstract class SSAOptimization extends OptimizationPass {
    private HashMap<String, CfgNodeSet> iteratedDFMap = null;
    private HashMap<Integer, String> defSetMap = null;
    private LiveVariableAnalysis lva;
    private HashMap<String, LinkedList<SSAVROperand>> nameStack;
    private int lastName[];
    private HashMap<String, LinkedList<IlocInstruction>> uses;
    private AvailTableStack availTabStack;
    private VirtualRegisterSet variables;

    public SSAOptimization(String prevPassA, String passA) {
        prevPassAbbrev = prevPassA;
        passAbbrev = passA;
    }

    public void emitCode(PrintWriter pw) {
        for (IlocRoutine rtn : routines)
            rtn.emitCode(pw);
    }

    public void emitCodeWithSSA(PrintWriter pw) {
        for (IlocRoutine rtn : routines)
            rtn.emitCodeWithSSA(pw);
    }

    private CfgNodeSet computeSv(Cfg cfg, int i) {
        CfgNodeSet s_v = new CfgNodeSet(cfg.getNodeMap());

        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
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

        for (int i = variables.nextSetBit(0); i >= 0; i = variables.nextSetBit(i + 1)) {
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

        for (int i = variables.nextSetBit(0); i >= 0; i = variables.nextSetBit(i + 1)) {
            CfgNodeSet idf = iteratedDFMap.get(defSetMap.get(i));
            idf.set(g.getEntryNode());
            for (int j = idf.nextSetBit(0); j >= 0; j = idf.nextSetBit(j + 1)) {
                BasicBlock b = (BasicBlock) idf.getCfgNode(j);
                if (lva.getInMap().get(b).get(i)) {
                    int n = b.getPreds().size();
                    Vector<Operand> operands = new Vector<Operand>(n);
                    for (int k = 0; k < n; k++)
                        operands.add(new VirtualRegisterOperand(i));
                    PhiNode p = new PhiNode(operands, new VirtualRegisterOperand(i));
                    b.addPhiNode(p);
                }
            }
        }
    }

    private LinkedList<SSAVROperand> getNameStackList(VirtualRegisterOperand vr) {
        LinkedList<SSAVROperand> list;
        String key = vr.toString();
        if ((list = nameStack.get(key)) == null) {
            list = new LinkedList<SSAVROperand>();
            nameStack.put(key, list);
        }
        return list;
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
            VirtualRegisterOperand t0 = p.getLValue();
            SSAVROperand t = newName(t0.getRegisterId());
            getNameStackList(t0).push(t);
        }

        availTabStack.startBlock();

        Iterator<IlocInstruction> bIter = b.iterator();
        while (bIter.hasNext()) {
            IlocInstruction inst = bIter.next();
            Vector<Operand> operands = inst.getRValues();
            for (int i = 0; i < operands.size(); i++) {
                Operand op = operands.elementAt(i);
                if (op instanceof VirtualRegisterOperand) {
                    VirtualRegisterOperand vr = (VirtualRegisterOperand) op;
                    inst.replaceOperandAtIndex(i, getNameStackList(vr).getFirst().copy());
                    getUseList(vr.getRegisterId()).add(inst);
                }
            }

            SSAVROperand svr = null;
            if (inst.isExpression() && ((svr = availTabStack.get(inst.getSSAAvailKey())) != null)) {
                VirtualRegisterOperand t = inst.getLValue();
                getNameStackList(t).push(svr);
                dead.add(inst);
            } else {
                VirtualRegisterOperand t = inst.getLValue();
                if (t != null) {
                    int index = t.getRegisterId();
                    SSAVROperand tn = newName(index);
                    getNameStackList(t).push(tn);
                    availTabStack.put(inst.getSSAAvailKey(), tn);
                }
            }

        }
        for (CfgEdge e : b.getSuccs()) {
            BasicBlock s = (BasicBlock) e.getSucc();
            int j = s.whichPredecessor(b);
            for (PhiNode p : s.getPhiNodes()) {
                VirtualRegisterOperand tj = (VirtualRegisterOperand) p.getRValues().elementAt(j);
                p.replaceOperandAtIndex(j, getNameStackList(tj).getFirst().copy());
                getUseList(tj.getRegisterId()).add(p);
            }
        }

        for (DominatorTreeEdge e : b.getDTChildren()) {
            BasicBlock c = (BasicBlock) e.getSucc();
            optRename(c);
        }

        ListIterator<IlocInstruction> rIter = b.reverseIterator();
        while (rIter.hasPrevious()) {
            IlocInstruction inst = rIter.previous();
            VirtualRegisterOperand vr = inst.getLValue();
            if (vr != null) {
                SSAVROperand x = getNameStackList(vr).pop().copy();
                if (dead.contains(inst)) {
                    b.removeWithIterator(rIter, inst);
                } else
                    inst.replaceLValue(x);
            }
        }

        for (PhiNode p : b.getPhiNodes()) {
            VirtualRegisterOperand vr = p.getLValue();
            p.replaceLValue(getNameStackList(vr).pop().copy());
        }

        availTabStack.endBlock();
    }

    private void rename(Cfg g) {
        nameStack = new HashMap<String, LinkedList<SSAVROperand>>();
        uses = new HashMap<String, LinkedList<IlocInstruction>>();
        lastName = new int[VirtualRegisterOperand.maxVirtualRegister + 1];
        availTabStack = new AvailTableStack();
        for (int i = 0; i <= VirtualRegisterOperand.maxVirtualRegister; i++) {
            lastName[i] = -1;
        }
        optRename((BasicBlock) g.getEntryNode());
    }

    private void removeSSA(Cfg g) {
        List<CfgNode> nl = g.getNodes();

        for (CfgNode n : nl) {
            BasicBlock b = (BasicBlock) n;
            Iterator<IlocInstruction> bIter = b.iterator();
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

    protected void computeSSAForm() {

        PrintWriter pw = null;
        for (IlocRoutine ir : getRoutines()) {

            ir.buildCfg();
            ir.getCfg().buildPreOrder();
            ir.getCfg().buildPostOrder();

            if (driver.CodeGenerator.emitCfg) {
                try {
                    pw = new PrintWriter(inputFileName + ".cfg.dot");
                    ir.getCfg().emitCfg(pw);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            buildVariableSet(ir);

            ir.getCfg().buildDT();
            if (driver.CodeGenerator.emitDT) {
                try {
                    pw = new PrintWriter(inputFileName + ".dt.dot");
                    ir.getCfg().emitDT(pw);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            ir.getCfg().buildDF();
            iteratedDFMap = new HashMap<String, CfgNodeSet>();
            defSetMap = new HashMap<Integer, String>();
            computeIDF(ir.getCfg());
            if (driver.CodeGenerator.emitDF) {
                try {
                    pw = new PrintWriter(inputFileName + ".df");
                    ir.getCfg().emitDF(pw);
                    emitIteratedDF(pw);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            lva = new LiveVariableAnalysis(VirtualRegisterOperand.maxVirtualRegister);
            lva.computeDFResult(ir.getCfg());
            if (driver.CodeGenerator.emitLVA) {
                try {
                    pw = new PrintWriter(inputFileName + ".lva");
                    emitLVA(pw, ir);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            insertPhiNodes(ir.getCfg());
            rename(ir.getCfg());
            if (driver.CodeGenerator.emitSSA) {
                try {
                    pw = new PrintWriter(inputFileName + ".ssa");
                    emitCodeWithSSA(pw);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void emitLVA(PrintWriter pwl, IlocRoutine ir) {
        for (BasicBlock b : ir.getBasicBlocks()) {
            pwl.println("Basic Block :" + b.getNodeId());

            if (lva.getInMap().get(b) != null)
                pwl.println("\tin  =  " + lva.getInMap().get(b).toString());
            else
                pwl.println("\tin  =  EmptySet");
            if (lva.getGenMap().get(b) != null)
                pwl.println("\tgen = " + lva.getGenMap().get(b).toString());
            else
                pwl.println("\tgen = EmptySet");
            if (lva.getPrsvMap().get(b) != null)
                pwl.println("\tprsv = " + lva.getPrsvMap().get(b).toString());
            else
                pwl.println("\tprsv = EmptySet");
            if (lva.getOutMap().get(b) != null)
                pwl.println("\tout = " + lva.getOutMap().get(b).toString() + "\n\n");
            else
                pwl.println("\tout = EmptySet");
        }
    }

    protected void computeNormalForm() {

        for (IlocRoutine ir : getRoutines())
            removeSSA(ir.getCfg());

    }

    private void buildVariableSet(IlocRoutine ir) {
        variables = new VirtualRegisterSet();
        for (BasicBlock b : ir.getBasicBlocks()) {
            Iterator<IlocInstruction> bIter = b.iterator();
            while (bIter.hasNext()) {
                IlocInstruction inst = bIter.next();
                for (Operand op : inst.getRValues()) {
                    if (op instanceof VirtualRegisterOperand)
                        variables.set((VirtualRegisterOperand) op);
                }
            }
        }
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
        }
        pw.println("");

        pw.println("\tIterated DF Map");
        pw.println("\t---------------");
        Set<String> idfKeys = iteratedDFMap.keySet();
        Iterator<String> idfki = idfKeys.iterator();
        while (idfki.hasNext()) {
            String s = idfki.next();
            pw.println("\t" + s + " -> " + iteratedDFMap.get(s).toString());
        }

        pw.println("");
    }

}