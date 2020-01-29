package middleEnd.utils;

public class CfgEdge {
    protected CfgNode pred;
    protected CfgNode succ;

    public CfgEdge() {

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