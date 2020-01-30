package middleEnd.utils;

import java.util.BitSet;
import java.util.HashMap;

public class DominatorSet extends BitSet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected HashMap<Integer, CfgNode> nodeMap;

    public DominatorSet(int size) {
        super(size);
        nodeMap = new HashMap<Integer, CfgNode>(size);
    }

    public DominatorSet(DominatorSet ds) {
        setNodeMap(ds.nodeMap);
        or(ds);
    }

    public void setNodeMap(HashMap<Integer, CfgNode> nm) {
        nodeMap = nm;
    }

    public CfgNode getCfgNode(int index) {
        return nodeMap.get(index);
    }

    public void setAll() {
        for (Integer n : nodeMap.keySet())
            set(n);
    }

    public void set(CfgNode n) {
        set(n.getNodeId());
    }

    public boolean get(CfgNode n) {
        return get(n.getNodeId());
    }

    public void clear(CfgNode n) {
        clear(n.getNodeId());
    }

    public void flip(CfgNode n) {
        flip(n.getNodeId());
    }

}