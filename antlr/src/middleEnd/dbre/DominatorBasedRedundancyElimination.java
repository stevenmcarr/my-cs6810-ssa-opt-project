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
        try {
            pw = new PrintWriter("cfg.gv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (IlocRoutine ir : getRoutines()) {
            ir.buildCfg();
            if (driver.CodeGenerator.emitCfg)
                ir.getCfg().emitGV(pw);
        }
        pw.close();
    }

}