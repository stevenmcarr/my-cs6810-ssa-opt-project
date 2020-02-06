package middleEnd.utils;

import java.util.BitSet;

import middleEnd.iloc.VirtualRegisterOperand;

public class VirtualRegisterSet extends BitSet {

    public VirtualRegisterSet() {
        super();
    }

    public VirtualRegisterSet(int size) {
        super(size);
    }

    public VirtualRegisterSet clone() {
        return (VirtualRegisterSet) super.clone();
    }

    public void clear(VirtualRegisterOperand vr) {
        clear(vr.getRegisterId());
    }

    public boolean get(VirtualRegisterOperand vr) {
        return get(vr.getRegisterId());
    }

    public void set(VirtualRegisterOperand vr) {
        set(vr.getRegisterId());
    }

    /**
     *
     */
    private static final long serialVersionUID = -2336436269307714256L;

}