package backend.ra;

import java.util.LinkedList;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.LiveRangeOperand;

public class InterferenceNode {

    private LinkedList<InterferenceNode> adjacentNodes = new LinkedList<InterferenceNode>();
    private LiveRangeOperand lr;
    private LinkedList<IlocInstruction> referenceInsts = new LinkedList<IlocInstruction>();
    private int color = -1;

    public InterferenceNode() {}

    public InterferenceNode addLR(LiveRangeOperand lr) {
        this.lr = lr;
        return this;
    }
    
    public InterferenceNode addInst(IlocInstruction inst) {
        if (!referenceInsts.contains(inst))
            referenceInsts.add(inst);
        return this;
    }

    public void addAdjacentNode(InterferenceNode neighbor) {
        if (!adjacentNodes.contains(neighbor))
            adjacentNodes.add(neighbor);
    }

    public LiveRangeOperand getLR() {
        return lr;
    }

    public boolean equals(InterferenceNode n) {
        return lr.equals(n.getLR());
    }

	public void setColor(int reg) {
        color = reg;
    }
    
    public int getColor() {
        return color;
    }

	public int getDegree() {
		return adjacentNodes.size();
	}

	public void removeEdges() {
        for (InterferenceNode n : adjacentNodes) {
            n.removeAdjacentNode(this);
        }
	}

    private void removeAdjacentNode(InterferenceNode interferenceNode) {
        adjacentNodes.remove(interferenceNode);
    }

    public LinkedList<InterferenceNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    public LinkedList<IlocInstruction> getReferenceInstructions() {
        return referenceInsts;
    }
}