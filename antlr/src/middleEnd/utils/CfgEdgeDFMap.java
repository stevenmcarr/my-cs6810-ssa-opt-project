package middleEnd.utils;

import java.util.HashMap;

public class CfgEdgeDFMap extends HashMap<Integer, DataFlowSet> {

	public CfgEdgeDFMap() {
		super();
	}

	public DataFlowSet get(CfgEdge e) {
		return super.get(e.getEdgeId());
	}

	public void put(CfgEdge e, DataFlowSet vrs) {
		super.put(e.getEdgeId(), vrs);
	}

	public boolean containsKey(CfgEdge e) {
		return super.containsKey(e.getEdgeId());
	}
}
