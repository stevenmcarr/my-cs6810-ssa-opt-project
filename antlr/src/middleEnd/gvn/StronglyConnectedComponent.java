package middleEnd.gvn;

import java.util.LinkedList;


public class StronglyConnectedComponent extends LinkedList<SSAGraphNode>{

    private int sccId;
    private static int numSCCs = 0;

    LinkedList<SSAGraphNode> reversePostorder = new LinkedList<SSAGraphNode>();
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public StronglyConnectedComponent() {
        super();
        sccId = numSCCs+1;
    }

    public int getSCCId() {
        return sccId;
    }
}