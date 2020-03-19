package middleEnd.utils;

public class VRInstPairSet extends DataFlowSet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public VRInstPairSet() { 
        super();
    }

    public VRInstPairSet(int size) {
        super(size);
    }

    public VRInstPairSet clone() {
        return (VRInstPairSet) super.clone();
    }

    public void clear(VRInstPair vr) {
        clear(vr.getPairId());
    }

    public boolean get(VRInstPair vr) {
        return get(vr.getPairId());
    }

    public void set(VRInstPair vr) {
        set(vr.getPairId());
    }

    public void clearAll() {
        clear(0,size());
    }
}