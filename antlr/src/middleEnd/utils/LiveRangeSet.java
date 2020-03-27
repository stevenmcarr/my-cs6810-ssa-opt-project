package middleEnd.utils;


import middleEnd.iloc.LiveRangeOperand;

public class LiveRangeSet extends DataFlowSet {

    public LiveRangeSet() {
        super();
    }

    public LiveRangeSet(int size) {
        super(size);
    }

    public LiveRangeSet clone() {
        return (LiveRangeSet) super.clone();
    }

    public void clear(LiveRangeOperand vr) {
        clear(vr.getLiveRangeId());
    }

    public boolean get(LiveRangeOperand vr) {
        return get(vr.getLiveRangeId());
    }

    public void set(LiveRangeOperand vr) {
        set(vr.getLiveRangeId());
    }

    /**
     *
     */
    private static final long serialVersionUID = -2336436269307714256L;

}