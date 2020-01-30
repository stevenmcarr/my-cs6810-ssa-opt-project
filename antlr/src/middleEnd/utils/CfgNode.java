package middleEnd.utils;

import java.util.ArrayList;
import java.util.List;

public class CfgNode {

    public static int cfgNodeId = 0;
    protected List<CfgEdge> preds;
    protected List<CfgEdge> succs;
    protected int nodeId;
    protected boolean marked = false;
    protected DominatorSet ds = null;
    protected DominatorTreeEdge idom = null; /* immediate dominator in the dominator tree */
    protected List<DominatorTreeEdge> children;

    public CfgNode() {
        preds = new ArrayList<CfgEdge>();
        succs = new ArrayList<CfgEdge>();
        children = new ArrayList<DominatorTreeEdge>();
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

    public void setMarked() {
        marked = true;
    }

    public void clearMarked() {
        marked = false;
    }

    public boolean isMarked() {
        return marked;
    }

    public void initDominatorSet(int size) {
        ds = new DominatorSet(size);
    }

    public DominatorSet getDominatorSet() {
        return ds;
    }

    public void setDominatorSet(DominatorSet new_ds) {
        ds = new_ds;
    }

    public void addDominatorTreeEdge(DominatorTreeEdge e) {
        children.add(e);
    }

    public void setImmediateDominator(CfgNode n) {
        if (n != null) {
            DominatorTreeEdge dte = new DominatorTreeEdge();
            dte.addPred(n).addSucc(this);
            n.addDominatorTreeEdge(dte);
            idom = new DominatorTreeEdge(dte);
        }
    }

    public List<DominatorTreeEdge> getDTChildren() {
        return children;
    }
}