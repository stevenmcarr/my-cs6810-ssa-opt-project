package middleEnd.gvn;

import java.util.LinkedList;

import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;

public class StronglyConnectedComponent extends LinkedList<SSAGraphNode> {

    private int sccId;
    private static int numSCCs = 0;
    private VirtualRegisterOperand newVR;

    LinkedList<SSAGraphNode> reversePostorder = new LinkedList<SSAGraphNode>();
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public StronglyConnectedComponent() {
        super();
        sccId = numSCCs++;
        newVR = new VirtualRegisterOperand(VirtualRegisterOperand.maxVirtualRegister++);
    }

    public int getSCCId() {
        return sccId;
    }

    public VirtualRegisterOperand getVR() {
        return newVR;
    }

	public boolean containsSVR(SSAVROperand svr) {
        boolean found = false;
        for (SSAGraphNode n : this) {
            if (n.getSSAVR().equals(svr)) {
                found = true;
                break;
            }
        }
		return found;
	}
}