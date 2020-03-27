package middleEnd.utils;

import java.util.BitSet;

public abstract class DataFlowSet extends BitSet {

    /**
     *
     */
    private static final long serialVersionUID = 6133757105401541047L;

    public DataFlowSet() {
        super();
    }

    public DataFlowSet(int size) {
        super(size);
    }
}