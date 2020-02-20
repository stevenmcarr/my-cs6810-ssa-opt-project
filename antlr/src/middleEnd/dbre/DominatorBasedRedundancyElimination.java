package middleEnd.dbre;

import middleEnd.utils.SSAOptimization;

public class DominatorBasedRedundancyElimination extends SSAOptimization {

    public DominatorBasedRedundancyElimination(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    /**
     * For dominator-based redundancy elimination, there is no additional work to be
     * done since this optimization is part of the renaming phase in computing SSA
     * form.
     */
    @Override
    protected void performSSAOptimization() {

    }

}