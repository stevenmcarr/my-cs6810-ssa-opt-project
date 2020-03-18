package middleEnd.utils;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;

public class VRInstPair {

    VirtualRegisterOperand vr;
    IlocInstruction inst;

    public VRInstPair() {}

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
}
