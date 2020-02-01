package middleEnd.utils;

import java.util.BitSet;
import java.util.HashMap;

public class CfgNodeSet extends BitSet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected static HashMap<Integer, CfgNode> nodeMap;

    public CfgNodeSet() {
        super();
    }

    public CfgNodeSet(int size) {
        super(size);
    }

    public CfgNodeSet(CfgNodeSet ds) {
        setNodeMap(nodeMap);
        or(ds);
    }

    public CfgNodeSet(HashMap<Integer,CfgNode> nm) {
        nodeMap = nm;
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