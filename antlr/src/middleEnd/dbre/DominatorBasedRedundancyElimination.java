package middleEnd.dbre;

import middleEnd.utils.SSAOptimization;

public class DominatorBasedRedundancyElimination extends SSAOptimization {

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {

        computeSSAForm();
        computeNormalForm();

    }

}