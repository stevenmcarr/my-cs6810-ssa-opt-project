package middleEnd.utils;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.SSAVROperand;

public class SSAVRInstPair {

    private static int numSSAVRInstPairs = 0;
    protected SSAVROperand vr;
    protected IlocInstruction inst;
    protected int pairId;
    protected boolean isDef = false;

    public SSAVRInstPair() {
        pairId = numSSAVRInstPairs++;
    }

    public SSAVRInstPair addSSAVR(SSAVROperand vr) {
        this.vr = vr;
        return this;
    }

    public SSAVRInstPair addInst(IlocInstruction inst) {
        this.inst = inst;
        return this;
    }

    public SSAVRInstPair addIsDef() {
        this.isDef = true;
        return this;
    } 

    public SSAVROperand getSSAVR() {
        return vr;
    }

    public IlocInstruction getInst() {
        return inst;
    }

    public boolean getIsDef() {
        return isDef;
    }

    public int getPairId() {
        return pairId;
    }

    public boolean isPairEqual(SSAVROperand vr,IlocInstruction i) {
        return (this.vr.equals(vr) && this.inst.equals(i));
    }

    public String toString() {
        return vr.toString()+inst.getStringRep();
    }
}
