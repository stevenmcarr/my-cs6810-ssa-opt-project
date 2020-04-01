package middleEnd.gvn;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class SSAGraph {

    public LinkedList<SSAGraphNode> nodes = new LinkedList<SSAGraphNode>();
    public HashMap<String,SSAGraphNode> defMap = new HashMap<String,SSAGraphNode> ();
    public LinkedList<SSAGraphNode> roots = new LinkedList<SSAGraphNode>();
    public LinkedList<DFSTree> forest = new LinkedList<DFSTree>();

    public SSAGraph() {}

    public void addNode(SSAGraphNode n) {
        nodes.add(n);
    }

    public void addDef(String name, SSAGraphNode n) {
        defMap.put(name,n);
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
}