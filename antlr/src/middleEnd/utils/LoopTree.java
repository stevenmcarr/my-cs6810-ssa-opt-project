package middleEnd.utils;

import java.util.LinkedList;

public class LoopTree {

    private LoopNode root;
    private Cfg cfg;

    public LoopTree() {}

    public void buildLoopTree() {
        root = new LoopNode();
        for (CfgNode n : cfg.getNodes() ) {
            BasicBlock b = (BasicBlock)n;
            LoopNode ln = (new LoopNode()).addBlock(b);
            b.setLoopNode(ln);
        }

        for (CfgNode n : cfg.getPostOrder()) {
            findLoop(n);
        }

        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock)n;
            LoopNode p = b.getLoopNode().getLoopParent();
            if (p != null) 
                p.setLoopParent(root);
        }
    }

    private void findLoop(CfgNode n) {
        LinkedList<LoopNode> loop = new LinkedList<LoopNode>();
        boolean found = false;
        for (CfgEdge e : n.getPreds()) {
            CfgNode p = e.getPred();
            if (n.dominates(p)) {
                BasicBlock pb = (BasicBlock)p;
                found = true;
                LoopNode pln = pb.getLoopNode();
                if (!loop.contains(pln) && !p.equals(n)) {
                    loop.add(pln);
                }
            }
        }
        if (found) 
            findBody(loop,n);
    }

    private void findBody(LinkedList<LoopNode> generators, CfgNode h) {
        LinkedList<LoopNode> loop = new LinkedList<LoopNode>();
        LinkedList<LoopNode> queue = new LinkedList<LoopNode>();
        for (LoopNode ln : generators) {
            LoopNode l = loopAncestor(ln);
            if (!loop.contains(l)) {
                loop.add(l);
                queue.addLast(l);
            }
        }
        while (!queue.isEmpty()) {
            LoopNode b = queue.removeFirst();
            for (CfgEdge e : b.getLoopEntry().getBlock().getPreds()) {
                CfgNode p = e.getPred();
                if (!p.equals(h)) {
                    LoopNode l1 = loopAncestor(p.getLoopNode());
                    if (!loop.contains(l1)) {
                        queue.addLast(l1);
                        loop.add(l1);
                    }
                }
            }
        }

        loop.add(h.getLoopNode());
        LoopNode x = new LoopNode();
        x.addLoop(loop); 
        x.setLoopEntry(h.getLoopNode());
        x.setLoopParent(null);
        for (LoopNode ln : loop) 
            ln.setLoopParent(x);
    }

    private LoopNode loopAncestor(LoopNode ln) {
        while (ln.getLoopParent() != null)
            ln = ln.getLoopParent();
        return ln;
    }

    public LoopTree addCfg(Cfg g) {
        cfg = g;
        return this;
    }

    public LoopNode getRoot() {
        return root;
    }
}