package middleEnd.utils;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;

public class VRInstPair {

    private static int numVRInstPairs = 0;
    private VirtualRegisterOperand vr;
    private IlocInstruction inst;
    private int pairId;

    public VRInstPair() {
        pairId = numVRInstPairs++;
    }

    public VRInstPair addVR(VirtualRegisterOperand vr) {
        this.vr = vr;
        return this;
    }

    public VRInstPair addInst(IlocInstruction inst) {
        this.inst = inst;
        return this;
    }

    public VirtualRegisterOperand getVR() {
        return vr;
    }

    public IlocInstruction getInst() {
        return inst;
    }

    public int getPairId() {
        return pairId;
    }

    public boolean isPairEqual(VirtualRegisterOperand vr,IlocInstruction i) {
        return (this.vr.equals(vr) && this.inst.equals(i));
    }
}
