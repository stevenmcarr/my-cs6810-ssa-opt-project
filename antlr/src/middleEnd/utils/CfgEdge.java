package middleEnd.utils;

public class CfgEdge {
    private static int numEdges = 0;
    protected CfgNode pred;
    protected CfgNode succ;
    private int edgeId;

    public CfgEdge() {
        edgeId = numEdges++;
    }

    public CfgEdge addPred(CfgNode n) {
        pred = n;
        return this;
    }

    public CfgEdge addSucc(CfgNode n) {
        succ = n;
        return this;
    }

    public CfgNode getPred() {
        return pred;
    }

    public CfgNode getSucc() {
        return succ;
    }
}