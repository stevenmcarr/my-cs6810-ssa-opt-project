package middleEnd.iloc;

import java.util.LinkedList;

import middleEnd.utils.SSAVRInstPair;

public class SSAVROperand extends VirtualRegisterOperand {

    private int subscript;

    private SSAVRInstPair ssaDef = null;
    private LinkedList<SSAVRInstPair> ssaUses = new LinkedList<SSAVRInstPair>();

    public SSAVROperand(int registerId) {
        super(registerId);
        subscript = -1;
    }

    public SSAVROperand(int registerId, int subscript) {
        super(registerId);
        this.subscript = subscript;
    }

    public int getSubscript() {
        return subscript;
    }

    public void setSubscript(int i) {
        subscript = i;
    }

    public String toString() {
        return "%vr" + registerId + "_" + subscript;
    }

    public SSAVROperand copy() {
        return new SSAVROperand(registerId, subscript);
    }

    public void setSSADef(SSAVRInstPair svr) {
        ssaDef = svr;
    }

    public SSAVRInstPair getSSADef() {
        return ssaDef;
    }

    public void addSSAUse(SSAVRInstPair svr) {
        ssaUses.add(svr);
    }

    public LinkedList<SSAVRInstPair> getSSAUses() {
        return ssaUses;
    }
}