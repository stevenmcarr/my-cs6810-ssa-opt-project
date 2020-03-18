package middleEnd.iloc;

public class LiveRangeOperand extends VirtualRegisterOperand {

    private static int numLiveRanges = 0;

    private int liveRangeId;

    public LiveRangeOperand(VirtualRegisterOperand vr) {
        super(vr.registerId);
        liveRangeId = numLiveRanges++;
        vr.copyDefsUsesToVR(this);
    }

    public LiveRangeOperand(int vrId) {
        super(vrId);
        liveRangeId = numLiveRanges++;
	}

	public int getLiveRangeId() {
        return liveRangeId;
    }

    public String toString() {
        return "%lr" + liveRangeId;
    }

    public boolean equals(LiveRangeOperand lr) {
        return liveRangeId == lr.getLiveRangeId();
    }
}