package middleEnd.utils;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Cfg {

    private CfgNode entryNode;
    private CfgNode exitNode;
    private List<CfgNode> nodes;
    private List<CfgNode> preOrder;
    private List<CfgNode> postOrder;

    public Cfg() {
        nodes = new LinkedList<CfgNode>();
    }

    public Cfg addEntryNode(CfgNode entry) {
        entryNode = entry;
        nodes.add(entry);
        return this;
    }

    public Cfg addExitNode(CfgNode exit) {
        exitNode = exit;
        nodes.add(exit);
        return this;
    }

    public Cfg addNode(CfgNode n) {
        nodes.add(n);
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

    public void emitGV(PrintWriter pw) {

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