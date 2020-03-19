package middleEnd.dce;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.JumpIInstruction;
import middleEnd.iloc.LabelOperand;
import middleEnd.iloc.Operand;
import middleEnd.iloc.PhiNode;
import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.CfgNodeSet;
import middleEnd.utils.IlocInstructionSet;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.SSAOptimization;

public class DeadCodeElimination extends SSAOptimization {

    public DeadCodeElimination(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    private void removeDeadCode() {
        for (IlocRoutine rtn : routines) {
            rtn.buildInstructionMap();
            buildDefMap(rtn);
            LinkedList<IlocInstruction> work = new LinkedList<IlocInstruction>();
            IlocInstructionSet necessary = new IlocInstructionSet(rtn.getInstructionMap());

            for (CfgNode n : rtn.getCfg().getNodes()) {
                Iterator<IlocInstruction> iter = ((BasicBlock) n).iterator();
                while (iter.hasNext()) {
                    IlocInstruction i = iter.next();
                    if (i.isNecessary()) {
                        necessary.set(i);
                        work.add(i);
                    }
                }
            }

            while (!work.isEmpty()) {
                IlocInstruction inst = work.removeFirst();
                BasicBlock b = inst.getBlock();
                CfgNodeSet cd = b.getPostDominanceFrontier();
                for (CfgEdge e : b.getPreds()) {
                    BasicBlock c = (BasicBlock) e.getPred();
                    if (cd.get(c)) {
                        IlocInstruction j = c.getLastInst();
                        if (j.isConditionalBranchInstruction() && !necessary.get(j)) {
                            necessary.set(j);
                            work.add(j);
                        }
                    }
                }
                Vector<Operand> rvals = inst.getRValues();
                for (Operand t : rvals) {
                    if (t instanceof SSAVROperand) {
                        IlocInstruction j = rtn.getDefinitionMap().get(t.toString());
                        if (!necessary.get(j)) {
                            necessary.set(j);
                            work.add(j);
                        }
                    }
                }
            }

            for (CfgNode n : rtn.getCfg().getNodes()) {
                ListIterator<IlocInstruction> iter = ((BasicBlock) n).listIterator();
                while (iter.hasNext()) {
                    IlocInstruction i = iter.next();
                    if (!necessary.get(i))
                        if (i.isConditionalBranchInstruction())
                            ((BasicBlock) n).replaceWithIterator(iter, i, changeBranchToIPdom(i));
                        else
                            ((BasicBlock) n).removeWithIterator(iter, i);
                }
            }
        }
    }

    private void buildDefMap(IlocRoutine rtn) {
        HashMap<String, IlocInstruction> definitionMap = new HashMap<String, IlocInstruction>();
        for (PhiNode p : ((BasicBlock) rtn.getCfg().getEntryNode()).getPhiNodes()) {
            definitionMap.put(p.getLValue().toString(), p);
        }
        for (BasicBlock b : rtn.getBasicBlocks()) {
            for (PhiNode p : b.getPhiNodes()) {
                definitionMap.put(p.getLValue().toString(), p);
            }
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                for (VirtualRegisterOperand op : inst.getAllLValues())
                    if (op instanceof SSAVROperand)
                        definitionMap.put(op.toString(), inst);
            }
        }

        rtn.setDefintionMap(definitionMap);
    }

    private IlocInstruction changeBranchToIPdom(IlocInstruction i) {
        BasicBlock ipdom = (BasicBlock) i.getBlock().getImmediatePostDominator();
        IlocInstruction first = ipdom.getFirstInst();
        String label = first.getLabel();
        LabelOperand labelOp = null;
        if (label == null) {
            labelOp = LabelOperand.makeLabelOperand();
            first.setLabel(labelOp.getLabel());
        } else
            labelOp = new LabelOperand(label);
        IlocInstruction inst = new JumpIInstruction(labelOp);

        return inst;
    }

    private void computePDF() {
        PrintWriter pw = null;
        if (driver.CodeGenerator.emitPDF) {
            try {
                pw = new PrintWriter(inputFileName + ".postdf");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (IlocRoutine rtn : routines) {
            rtn.getCfg().buildPDF();
            if (driver.CodeGenerator.emitPDF) {
                rtn.getCfg().emitPDF(pw);
            }
        }
        if (driver.CodeGenerator.emitPDF)
            pw.close();
    }

    private void computePDT() {
        PrintWriter pw = null;
        if (driver.CodeGenerator.emitPDT) {
            try {
                pw = new PrintWriter(inputFileName + ".pdt.dot");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (IlocRoutine rtn : routines) {
            rtn.getCfg().buildPDT();
            if (driver.CodeGenerator.emitPDT) {
                rtn.getCfg().emitPDT(pw);
            }
        }
        if (driver.CodeGenerator.emitPDT)
            pw.close();
    }

    @Override
    protected void performSSAOptimization() {
        computePDT();
        computePDF();
        removeDeadCode();
    }

}