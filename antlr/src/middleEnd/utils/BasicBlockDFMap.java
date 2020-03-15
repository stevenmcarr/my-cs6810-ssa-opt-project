package middleEnd.utils;

import java.util.HashMap;

public class BasicBlockDFMap extends HashMap<Integer, DataFlowSet> {

    /**
     *
     */
    private static final long serialVersionUID = 5465896326574072161L;

    public BasicBlockDFMap() {
        super();
    }

    public DataFlowSet get(BasicBlock b) {
        return super.get(b.getNodeId());
    }

    public void put(BasicBlock b, DataFlowSet vrs) {
        super.put(b.getNodeId(), vrs);
    }
}