package middleEnd.dbre;

import middleEnd.utils.OptimizationPass;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import middleEnd.utils.IlocRoutine;

public class DominatorBasedRedundancyElimination extends OptimizationPass {

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        prevPassAbbrev = prevPassA;
        passAbbrev = passA;
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
            ir.getCfg().buildPreOrder();
            ir.getCfg().buildPostOrder();
            ir.getCfg().buildDT();
            ir.getCfg().buildDF();
            if (driver.CodeGenerator.emitCfg)
                ir.getCfg().emitCfg(pw);
            if (driver.CodeGenerator.emitDT)
                ir.getCfg().emitDT(pw);
            if (driver.CodeGenerator.emitDF)
                ir.getCfg().emitDF(pw2);
        }
        pw.close();
        pw2.close();
    }

}