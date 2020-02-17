package middleEnd.dce;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleEnd.iloc.IlocInstruction;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.SSAOptimization;

public class DeadCodeElimination extends SSAOptimization {

    public DeadCodeElimination(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {
        computeSSAForm();
        computePDT();
        computePDF();
        removeDeadCode();
        computeNormalForm();
    }

    private void removeDeadCode() {
        for (IlocRoutine rtn : routines) {
            List<IlocInstruction> work = new ArrayList<IlocInstruction>();
            List<IlocInstruction> necessary = new ArrayList<IlocInstruction>();

            for (CfgNode n : rtn.getCfg().getNodes()) {
                Iterator<IlocInstruction> iter = ((BasicBlock) n).iterator();
                while (iter.hasNext()) {
                    IlocInstruction i = iter.next();
                    if (i.isNecessary()) {
                        necessary.add(i);
                        work.add(i);
                    }
                }
            }
        }
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
        pw.close();
    }

}