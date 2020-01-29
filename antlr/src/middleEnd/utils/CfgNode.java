package middleEnd.utils;

import java.util.ArrayList;
import java.util.List;

public class CfgNode {

    public static int cfgNodeId = 0;
    private List<CfgEdge> preds;
    private List<CfgEdge> succs;
    private int nodeId;

    public CfgNode() {
        preds = new ArrayList<CfgEdge>();
        succs = new ArrayList<CfgEdge>();
        nodeId = cfgNodeId++;
    }

    public CfgNode addPredecessorEdge(CfgNode pred, CfgNode succ) {
        CfgEdge edge = new CfgEdge();
        preds.add(edge.addPred(pred).addSucc(succ));
        return this;
    }

    public CfgNode addSuccessorEdge(CfgNode pred, CfgNode succ) {
        CfgEdge edge = new CfgEdge();
        succs.add(edge.addPred(pred).addSucc(succ));
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
}