package middleEnd.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Cfg {

    private CfgNode entryNode;
    private CfgNode exitNode;
    private List<CfgNode> nodes;
    private List<CfgNode> preOrder;
    private List<CfgNode> postOrder;
    private int maxIndex = 0;

    public Cfg() {
        nodes = new LinkedList<CfgNode>();
    }

    public Cfg addEntryNode(CfgNode entry) {
        entryNode = entry;
        nodes.add(entry);
        maxIndex = maxIndex > entry.getNodeId() ? maxIndex : entry.getNodeId();
        return this;
    }

    public Cfg addExitNode(CfgNode exit) {
        exitNode = exit;
        nodes.add(exit);
        maxIndex = maxIndex > exit.getNodeId() ? maxIndex : exit.getNodeId();
        return this;
    }

    public Cfg addNode(CfgNode n) {
        nodes.add(n);
        maxIndex = maxIndex > n.getNodeId() ? maxIndex : n.getNodeId();
        return this;
    }

    public CfgNode getEntryNode() {
        return entryNode;
    }

    public CfgNode getExitNode() {
        return exitNode;
    }

    public List<CfgNode> getPreOrder() {
        return preOrder;
    }

    public List<CfgNode> getPostOrder() {
        return postOrder;
    }

    private void walkPreOrder(CfgNode n) {
        for (CfgEdge e : n.getPreds()) {
            CfgNode p = e.getPred();
            if (!p.isMarked()) {
                p.setMarked();
                walkPreOrder(p);
            }
        }
        preOrder.add(n);
    }

    public void buildPreOrder() {
        preOrder = new ArrayList<CfgNode>();
        for (CfgNode n : nodes)
            n.clearMarked();
        walkPreOrder(exitNode);
    }

    private void walkPostOrder(CfgNode n) {
        for (CfgEdge e : n.getSuccs()) {
            CfgNode s = e.getSucc();
            if (!s.isMarked()) {
                s.setMarked();
                walkPostOrder(s);
            }
        }
        postOrder.add(n);
    }

    public void buildPostOrder() {
        postOrder = new ArrayList<CfgNode>();
        for (CfgNode n : nodes)
            n.clearMarked();
        walkPostOrder(entryNode);
    }

    public void buildDT() {
        HashMap<Integer, CfgNode> nodeMap = new HashMap<Integer, CfgNode>();

        for (CfgNode n : preOrder)
            nodeMap.put(n.getNodeId(), n);

        List<CfgNode> work = new ArrayList<CfgNode>(preOrder);
        work.remove(entryNode);

        entryNode.initDominatorSet(maxIndex);
        entryNode.getDominatorSet().setNodeMap(nodeMap);
        entryNode.getDominatorSet().set(entryNode);

        for (CfgNode n : work) {
            n.initDominatorSet(maxIndex);
            DominatorSet ds = n.getDominatorSet();
            ds.setNodeMap(nodeMap);
            ds.setAll();
        }

        boolean changed;
        do {
            changed = false;
            for (CfgNode n : work) {
                DominatorSet ds = n.getDominatorSet();
                DominatorSet temp = new DominatorSet(ds);
                temp.setAll();
                for (CfgEdge e : n.getPreds()) {
                    CfgNode p = e.getPred();
                    temp.and(p.getDominatorSet());
                }
                temp.set(n);
                if (!ds.equals(temp)) {
                    changed = true;
                    n.setDominatorSet(temp);
                }
            }

        } while (changed);

        entryNode.setImmediateDominator(null);
        for (CfgNode n : work) {
            DominatorSet ds = new DominatorSet(n.getDominatorSet());
            ds.clear(n);
            int minIndex = ds.nextSetBit(0);
            CfgNode idom = ds.getCfgNode(minIndex);

            for (int i = ds.nextSetBit(minIndex + 1); i >= 0; i = ds.nextSetBit(i + 1)) {
                CfgNode next = ds.getCfgNode(i);
                if (next.getDominatorSet().cardinality() > idom.getDominatorSet().cardinality()) {
                    idom = next;
                }
            }

            n.setImmediateDominator(idom);
        }
    }

    public void emitCfg(PrintWriter pw) {

        pw.println("digraph cfg {");

        for (CfgNode n : nodes) {
            pw.println(n.getNodeId() + " [ label = \"" + n.getCfgNodeLabel().replace('"', ' ') + "\", shape = square]");
            for (CfgEdge e : n.getPreds()) {
                pw.println(e.getPred().getNodeId() + "->" + e.getSucc().getNodeId());
            }
        }

        pw.println("}");

    }

    public void emitDT(PrintWriter pw) {

        pw.println("digraph dt {");

        for (CfgNode n : nodes) {
            pw.println(n.getNodeId() + " [shape = square]");
            for (DominatorTreeEdge e : n.getDTChildren()) {
                pw.println(e.getPred().getNodeId() + "->" + e.getSucc().getNodeId());
            }
        }

        pw.println("}");

    }
}