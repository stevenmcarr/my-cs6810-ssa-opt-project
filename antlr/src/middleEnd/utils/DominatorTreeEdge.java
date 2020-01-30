package middleEnd.utils;

public class DominatorTreeEdge {
    private static int numEdges = 0;
    protected CfgNode pred;
    protected CfgNode succ;
    private int edgeId;

    public DominatorTreeEdge() {
        edgeId = numEdges++;
    }

    public DominatorTreeEdge(DominatorTreeEdge e) {
        edgeId = numEdges++;
        pred = e.getPred();
        succ = e.getSucc();
    }

    public DominatorTreeEdge addPred(CfgNode n) {
        pred = n;
        return this;
    }

    public DominatorTreeEdge addSucc(CfgNode n) {
        succ = n;
        return this;
    }

    public CfgNode getPred() {
        return pred;
    }

    public CfgNode getSucc() {
        return succ;
    }

    public int getEdgeId() {
        return edgeId;
    }
}