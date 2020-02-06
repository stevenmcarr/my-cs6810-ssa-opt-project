package middleEnd.utils;

import java.util.HashMap;

public class BasicBlockDFMap extends HashMap<Integer, VirtualRegisterSet> {

    /**
     *
     */
    private static final long serialVersionUID = 5465896326574072161L;

    public BasicBlockDFMap() {
        super();
    }

    public VirtualRegisterSet get(BasicBlock b) {
        return super.get(b.getNodeId());
    }

    public void put(BasicBlock b, VirtualRegisterSet vrs) {
        super.put(b.getNodeId(), vrs);
    }
}