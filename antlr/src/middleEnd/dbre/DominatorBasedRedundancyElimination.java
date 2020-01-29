package middleEnd.dbre;

import middleEnd.utils.OptimizationPass;

import java.util.Vector;

import middleEnd.utils.IlocRoutine;

public class DominatorBasedRedundancyElimination extends OptimizationPass {

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        prevPassAbbrev = prevPassA;
        passAbbrev = passA;
    }

    @Override
    protected void optimizeCode() {

        for (IlocRoutine ir : getRoutines()) {
            ir.buildCfg();
        }
    }

}