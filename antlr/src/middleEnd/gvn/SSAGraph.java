package middleEnd.gvn;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;

import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;

import java.util.List;

public class SSAGraph {

    public LinkedList<SSAGraphNode> nodes = new LinkedList<SSAGraphNode>();
    public HashMap<String, SSAGraphNode> defMap = new HashMap<String, SSAGraphNode>();
    public LinkedList<SSAGraphNode> roots = new LinkedList<SSAGraphNode>();
    public LinkedList<DFSTree> forest = new LinkedList<DFSTree>();

    public SSAGraph() {
    }

    public void addNode(SSAGraphNode n) {
        nodes.add(n);
    }

    public void addDef(String name, SSAGraphNode n) {
        defMap.put(name, n);
    }

    public SSAGraphNode getSSAGraphNodeDef(String name) {
        return defMap.get(name);
    }

    public void computeDFSForest() {
        clearVisited();
        for (SSAGraphNode n : nodes) {
            if (n.getPreds().isEmpty())
                roots.add(n);
        }
        for (SSAGraphNode n : roots) {
            forest.add(computeDFSTree(n));
        }
    }

    private DFSTree computeDFSTree(SSAGraphNode n) {
        n.setVisited();
        DFSTree t = new DFSTree();
        t.add(n);
        for (SSAGraphNode c : n.getAdjacentNodes()) {
            if (!c.hasBeenVisited())
                t.addAll(computeDFSTree(c));
        }
        return t;
    }

    private void clearVisited() {
        for (SSAGraphNode n : nodes)
            n.clearVisited();
    }

    public LinkedList<DFSTree> getForest() {
        return forest;
    }

    public LinkedList<SSAGraphNode> getNodes() {
        return nodes;
    }

    /**
     * 
     * The follow code computes strongly connected components using Tarjan's algorithm
     * The code is modified from https://github.com/thai321/Algorithm-Problems-Java/tree/master/Strongly%20Connected%20Components%20(SCC)
     * 
     */
    private Stack<SSAGraphNode> stack;
    private List<SSAGraphNode> vertices;
    private List<StronglyConnectedComponent> connectedComponentList;
    private int time = 0; // keep the order of node we've visited

    public void computeSCCTarjanAlgorithm() {
        this.stack = new Stack<SSAGraphNode>();
        this.vertices = nodes;
        this.connectedComponentList = new ArrayList<StronglyConnectedComponent>();
        runTarjanAlgorithm();
    }

    public void runTarjanAlgorithm() {
        for (SSAGraphNode vertex : this.vertices)
            if (!vertex.hasBeenVisited())
                dfs(vertex);
    }

    private void dfs(SSAGraphNode vertex) {
        vertex.setLowLink(time++);
        // System.out.println("Visiting vertex " + vertex + " with lowLink " + vertex.getLowLink());
        vertex.setVisited();
        this.stack.push(vertex);
        boolean isComponentRoot = true;

        for (SSAGraphNode v : vertex.getAdjacentNodes()) {

            if (!v.hasBeenVisited()) {
                // System.out.println("Recursively visit vertex " + v);
                dfs(v);
            }

            // we have a back edge
            if (vertex.getLowLink() > v.getLowLink()) {
                // System.out.println("\nBecause vertex " + vertex + " lowLink (value:" + vertex.getLowLink()
                //         + ") > vertex " + v + " lowLink (value:" + v.getLowLink() + ") --> we set vertex " + vertex
                //         + " lowLink = vertex " + v + " lowLink value which is " + v.getLowLink());

                vertex.setLowLink(v.getLowLink());
                // System.out.println("So SSAGraphNode " + vertex + " is not the root of a SCC\n");
                isComponentRoot = false;
            }
        }

        // only for the root SCC node
        if (isComponentRoot) {

            // System.out.println("SSAGraphNode " + vertex + " is the root of an SCC");
            StronglyConnectedComponent component = new StronglyConnectedComponent();

            while (true) {
                SSAGraphNode actualSSAGraphNode = stack.pop();
                // System.out.println("So vertex " + actualSSAGraphNode + " is in SCC " + count);
                component.add(actualSSAGraphNode);
                actualSSAGraphNode.setLowLink(Integer.MAX_VALUE);

                // Run util it hits the root SCC node
                if (actualSSAGraphNode.getPairId() == vertex.getPairId())
                    break;
            }

            // System.out.println("\n---------------------\n");
            connectedComponentList.add(component);
        }
    }

    public void printComponents() {
        System.out.println(connectedComponentList);
    }

    public List<StronglyConnectedComponent> getSCCs() {
        return connectedComponentList;
    }

    public void emit(PrintWriter pw) {
        pw.println("digraph ssa {");

        for (SSAGraphNode n : nodes) {
            pw.println(n.getPairId() + " [ label = \"" + n.getSSAVR().toString() + "\", shape = circle]");
            for (SSAGraphNode m : n.getAdjacentNodes()) {
                pw.println(n.getPairId() + "->" + m.getPairId());
            }
        }

        pw.println("}");
    }

    public VirtualRegisterOperand getSCCVR(SSAVROperand svr) {
        VirtualRegisterOperand vr = null;
        for (StronglyConnectedComponent scc : connectedComponentList) {
            if (scc.containsSVR(svr)) {
                vr = scc.getVR();
                break;
            }
        }

        return vr;
    }
}