package middleEnd.dbre;

import middleEnd.utils.OptimizationPass;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.CfgNodeSet;
import middleEnd.utils.IlocRoutine;

public class DominatorBasedRedundancyElimination extends OptimizationPass {

    private HashMap<Integer, CfgNodeSet> iteratedDF = new HashMap<Integer, CfgNodeSet>();

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        prevPassAbbrev = prevPassA;
        passAbbrev = passA;
    }

    private CfgNodeSet computeSv(Cfg cfg, int i) {
        CfgNodeSet s_v = new CfgNodeSet(cfg.getNodeMap());

        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            for (IlocInstruction inst = b.getFirstInst(); inst != null
                    && inst != b.getLastInst().getNextInst(); inst = inst.getNextInst()) {
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

        for (int i = 0; i <= maxVirtualRegister; i++) {
            CfgNodeSet idf_v = new CfgNodeSet(cfg.getNodeMap());
            CfgNodeSet s_v = computeSv(cfg, i);
            if (!s_v.isEmpty()) {
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
            }
            iteratedDF.put(i, idf_v);
        }
    }

    @Override
    protected void optimizeCode() {

        PrintWriter pw = null;
        PrintWriter pw2 = null;
        try {
            pw = new PrintWriter("graph.dot");
            pw2 = new PrintWriter("graph.df");
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
        }
        pw.close();
        pw2.close();
    }

    private void emitIteratedDF(PrintWriter pw) {
        pw.println("Iterated Dominance Frontiers");
        pw.println("----------------------------");
        Set<Integer> keys = iteratedDF.keySet();
        Iterator<Integer> ki = keys.iterator();
        while (ki.hasNext()) {
            int i = ki.next();
            pw.println(i + ": " + iteratedDF.get(i).toString());
            ki.remove();
        }
        pw.println("");
    }

}