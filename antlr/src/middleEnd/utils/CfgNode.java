package middleEnd.utils;

import java.util.ArrayList;
import java.util.List;

public class CfgNode {

    public static int cfgNodeId = 0;
    protected List<CfgEdge> preds;
    protected List<CfgEdge> succs;
    protected int nodeId;

    public CfgNode() {
        preds = new ArrayList<CfgEdge>();
        succs = new ArrayList<CfgEdge>();
        nodeId = cfgNodeId++;
    }

    public CfgNode addPredecessorEdge(CfgNode pred) {
        CfgEdge edge = new CfgEdge();
        preds.add(edge.addPred(pred).addSucc(this));
        return this;
    }

    public CfgNode addSuccessorEdge(CfgNode succ) {
        CfgEdge edge = new CfgEdge();
        succs.add(edge.addPred(this).addSucc(succ));
        return this;
    }

    public List<CfgEdge> getPreds() {
        return preds;
    }

    public List<CfgEdge> getSuccs() {
        return succs;
    }

    public int getNodeId() {
        return nodeId;
    }

    public String getCfgNodeLabel() {
        return Integer.toString(nodeId);
    }
}