package middleEnd.utils;

import java.util.LinkedList;

public class LoopNode {

    private BasicBlock block = null;
    private LinkedList<LoopNode> children = new LinkedList<LoopNode>();
    private LoopNode loopParent;
    private LoopNode loopEntry;

    public LoopNode() {

    }

    public LoopNode addBlock(BasicBlock b) {
        block = b;
        loopParent = null;
        loopEntry = this;
        return this;
    }

    public LoopNode addChild(LoopNode c) {
        children.add(c);
        return this;
    }

    public BasicBlock getBlock() {
        return block;
    }

    public LinkedList<LoopNode> getChildren() {
        return children;
    }

    public LoopNode getLoopParent() {
        return loopParent;
    }

    public void setLoopParent(LoopNode n) {
        loopParent = n;
    }

    public void setLoopEntry(LoopNode n) {
        loopEntry = n;
    }

    public LoopNode getLoopEntry() {
        return loopEntry;
    }

    public void addLoop(LinkedList<LoopNode> l) {
        children.addAll(l);
    }

    public int getNestingDepth() {
        int depth = 0;
        LoopNode ln = this;
        while (ln.getLoopParent() != null) {
            depth++;
            ln = ln.getLoopParent();
        }
        return depth;
    }
}
