package middleEnd.iloc;

public class LiveRangeOperand extends VirtualRegisterOperand {

    private int liveRangeId;

    public LiveRangeOperand(int registerId) {
        super(registerId);
    }

    public LiveRangeOperand addLiveRangeId(int lri) {
        liveRangeId = lri;
        return this;
    }
}