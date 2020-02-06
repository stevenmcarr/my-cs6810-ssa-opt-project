package middleEnd.iloc;

public class SSAVROperand extends VirtualRegisterOperand {

    private int subscript;

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
}