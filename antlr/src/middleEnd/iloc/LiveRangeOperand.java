package middleEnd.iloc;

public class LiveRangeOperand extends VirtualRegisterOperand {

    public static int numLiveRanges = 0;

    private int liveRangeId;

    public LiveRangeOperand(LiveRangeOperand lro) {
        super(lro.getRegisterId());
        liveRangeId = lro.getLiveRangeId();
        lro.copyInstanceVars(this);
    }

    public LiveRangeOperand() {
    }

    public LiveRangeOperand(VirtualRegisterOperand vr) {
        super(vr.registerId);
        liveRangeId = numLiveRanges++;
        vr.copyInstanceVars(this);
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

    public Operand deepCopy() {
        LiveRangeOperand lro = new LiveRangeOperand(this);
        copyInstanceVars(lro);
        return lro;
    }

    protected void copyInstanceVars(LiveRangeOperand copy) {
        copy.liveRangeId = liveRangeId;
        super.copyInstanceVars(copy);
    }
}