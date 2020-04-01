package middleEnd.gvn;

import java.util.LinkedList;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.SSAVROperand;
import middleEnd.utils.SSAVRInstPair;

public class SSAGraphNode extends SSAVRInstPair {

    private LinkedList<SSAGraphNode> adjacentNodes = new LinkedList<SSAGraphNode>();
    private LinkedList<SSAGraphNode> preds = new LinkedList<SSAGraphNode>();
    private boolean visited = false;

    public SSAGraphNode() {}

	public void addAdjacentNode(SSAGraphNode n) {
        adjacentNodes.add(n);
        n.addPred(this);
    }

    public SSAGraphNode addInst(IlocInstruction inst) {
        return (SSAGraphNode)super.addInst(inst);
    }

    public SSAGraphNode addSSAVR(SSAVROperand svr) {
        return (SSAGraphNode)super.addSSAVR(svr);
    }

    public LinkedList<SSAGraphNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addPred(SSAGraphNode n) {
        preds.add(n);
    }

    public LinkedList<SSAGraphNode> getPreds() {
        return preds;
    }

    public void setVisited() {
        visited = true;
    }

    public void clearVisited() {
        visited = false;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public String toString() {
        return inst.getStringRep();
    }
}
