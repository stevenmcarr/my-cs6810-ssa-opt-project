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
    private HashMap<Integer, CfgNode> nodeMap = new HashMap<Integer, CfgNode>();

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

    public List<CfgNode> getNodes() {
        return nodes;
    }

    public List<CfgNode> getPreOrder() {
        return preOrder;
    }

    public List<CfgNode> getPostOrder() {
        return postOrder;
    }

    public HashMap<Integer, CfgNode> getNodeMap() {
        return nodeMap;
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

        for (CfgNode n : preOrder)
            nodeMap.put(n.getNodeId(), n);

        List<CfgNode> work = new ArrayList<CfgNode>(preOrder);
        work.remove(entryNode);

        entryNode.getDominators().setNodeMap(nodeMap);
        entryNode.getDominators().set(entryNode);

        for (CfgNode n : work) {
            CfgNodeSet ds = n.getDominators();
            ds.setNodeMap(nodeMap);
            ds.setAll();
        }

        boolean changed;
        do {
            changed = false;
            for (CfgNode n : work) {
                CfgNodeSet ds = n.getDominators();
                CfgNodeSet temp = new CfgNodeSet(ds);
                temp.setAll();
                for (CfgEdge e : n.getPreds()) {
                    CfgNode p = e.getPred();
                    temp.and(p.getDominators());
                }
                temp.set(n);
                if (!ds.equals(temp)) {
                    changed = true;
                    n.setDominators(temp);
                }
            }

        } while (changed);

        entryNode.setImmediateDominator(null);
        for (CfgNode n : work) {
            CfgNodeSet ds = new CfgNodeSet(n.getDominators());
            ds.clear(n);
            int minIndex = ds.nextSetBit(0);
            CfgNode idom = ds.getCfgNode(minIndex);

            for (int i = ds.nextSetBit(minIndex + 1); i >= 0; i = ds.nextSetBit(i + 1)) {
                CfgNode next = ds.getCfgNode(i);
                if (next.getDominators().cardinality() > idom.getDominators().cardinality()) {
                    idom = next;
                }
            }

            n.setImmediateDominator(idom);
        }
    }

    private void buildDFForNode(CfgNode n) {
        n.getDominanceFrontier().setNodeMap(nodeMap);
        for (DominatorTreeEdge e : n.getDTChildren()) {
            CfgNode c = e.getSucc();
            CfgNodeSet df = c.getDominanceFrontier();
            for (int i = df.nextSetBit(0); i >= 0; i = df.nextSetBit(i + 1)) {
                CfgNode m = df.getCfgNode(i);
                if (!n.strictlyDominates(m))
                    n.getDominanceFrontier().set(m);
            }
        }
        for (CfgEdge e : n.getSuccs()) {
            CfgNode m = e.getSucc();
            if (!n.strictlyDominates(m))
                n.getDominanceFrontier().set(m);
        }
    }

    private void walkPostOrderDTToBuildDF(CfgNode n) {
        for (DominatorTreeEdge e : n.getDTChildren()) {
            CfgNode c = e.getSucc();
            walkPostOrderDTToBuildDF(c);
        }
        buildDFForNode(n);
    }

    public void buildDF() {
        for (CfgNode n : nodes)
            n.clearMarked();
        walkPostOrderDTToBuildDF(entryNode);
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

    public void emitDF(PrintWriter pw) {
        pw.println("Dominance Frontiers");
        pw.println("-------------------");
        for (CfgNode n : nodes) {
            pw.println("Node " + n.getNodeId() + ": " + n.getDominanceFrontier().toString());
        }
        pw.println("");
    }

    public void insertPhiNode() {
    }
}