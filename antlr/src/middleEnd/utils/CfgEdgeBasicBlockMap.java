package middleEnd.utils;

import java.util.HashMap;

public class CfgEdgeBasicBlockMap extends HashMap<Integer, BasicBlock> {

	public CfgEdgeBasicBlockMap() {
		super();
	}

	public BasicBlock get(CfgEdge e) {
		return super.get(e.getEdgeId());
	}

	public void put(CfgEdge e, BasicBlock b) {
		super.put(e.getEdgeId(), b);
	}

	public boolean containsKey(CfgEdge e) {
		return super.containsKey(e.getEdgeId());
	}
}
