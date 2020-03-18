package middleEnd.utils;

import java.util.ArrayList;
import java.util.List;

public class CfgNode {

    public static int cfgNodeId = 0;
    protected List<CfgEdge> preds;
    protected List<CfgEdge> succs;
    protected int nodeId;
    protected boolean marked = false;
    protected CfgNodeSet dominators = new CfgNodeSet();
    protected CfgNodeSet postDominators = new CfgNodeSet();
    protected CfgNodeSet dominanceFrontier = new CfgNodeSet();
    protected CfgNodeSet postDominanceFrontier = new CfgNodeSet();
    protected DominatorTreeEdge idom = null; /* immediate dominator in the dominator tree */
    protected DominatorTreeEdge ipdom = null; /* immediate post dominator in the dominator tree */
    protected List<DominatorTreeEdge> dtChildren;
    protected List<DominatorTreeEdge> pdtChildren;
    protected LoopNode ln = null;

    public CfgNode() {
        preds = new ArrayList<CfgEdge>();
        succs = new ArrayList<CfgEdge>();
        dtChildren = new ArrayList<DominatorTreeEdge>();
        pdtChildren = new ArrayList<DominatorTreeEdge>();
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

    public CfgNodeSet getDominators() {
        return dominators;
    }

    public CfgNodeSet getPostDominators() {
        return postDominators;
    }

    public void setDominators(CfgNodeSet new_ds) {
        dominators = new_ds;
    }

    public void setPostDominators(CfgNodeSet new_pds) {
        postDominators = new_pds;
    }

    public void addDominatorTreeEdge(DominatorTreeEdge e) {
        dtChildren.add(e);
    }

    public void addPostDominatorTreeEdge(DominatorTreeEdge e) {
        pdtChildren.add(e);
    }

    public void setImmediateDominator(CfgNode n) {
        if (n != null) {
            DominatorTreeEdge dte = new DominatorTreeEdge();
            dte.addPred(n).addSucc(this);
            n.addDominatorTreeEdge(dte);
            idom = new DominatorTreeEdge(dte);
        }
    }

    public void setImmediatePostDominator(CfgNode n) {
        if (n != null) {
            DominatorTreeEdge dte = new DominatorTreeEdge();
            dte.addPred(n).addSucc(this);
            n.addPostDominatorTreeEdge(dte);
            ipdom = new DominatorTreeEdge(dte);
        }
    }

    public List<DominatorTreeEdge> getDTChildren() {
        return dtChildren;
    }

    public List<DominatorTreeEdge> getPDTChildren() {
        return pdtChildren;
    }

    public CfgNodeSet getDominanceFrontier() {
        return dominanceFrontier;
    }

    public CfgNodeSet getPostDominanceFrontier() {
        return postDominanceFrontier;
    }

    public void setdominanceFrontier(CfgNodeSet new_ds) {
        dominanceFrontier = new_ds;
    }

    public void setPostDominanceFrontier(CfgNodeSet new_pds) {
        postDominanceFrontier = new_pds;
    }

    public boolean dominates(CfgNode m) {
        return m.getDominators().get(this);
    }

    public boolean postDominates(CfgNode m) {
        return m.getPostDominators().get(this);
    }

    public boolean strictlyDominates(CfgNode m) {
        return !equals(m) && dominates(m);
    }

    public boolean strictlyPostDominates(CfgNode m) {
        return !equals(m) && postDominates(m);
    }

    public int whichPredecessor(CfgNode n) {
        int i = 0;
        int nId = n.getNodeId();
        int whichPred = -1;
        for (CfgEdge e : preds) {
            if (e.getPred().getNodeId() == nId) {
                whichPred = i;
                break;
            }
            i++;
        }

        return whichPred;
    }

    public CfgNode getImmediateDominator() {
        return idom.getPred();
    }

    public CfgNode getImmediatePostDominator() {
        return ipdom.getPred();
    }

  public void setLoopNode(LoopNode ln2) {
    ln = ln2;
  }

  public LoopNode getLoopNode() {
    return ln;
  }

}