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

}