package middleEnd.dfa;

import java.util.List;

import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;

public abstract class BackwardDataFlowProblem extends IterativeFramework {

	@Override
	public DataFlowSet getCurrentMeetResult(BasicBlock n) {
		return outMap.get(n);
	}

	@Override
	public void setMeetResult(BasicBlock n, DataFlowSet vrs) {
		outMap.put(n, vrs);
	}

	@Override
	public void setTransferResult(BasicBlock n, DataFlowSet vrs) {
		inMap.put(n, vrs);
	}

	@Override
	public List<CfgNode> getNodeOrder(Cfg g) {
		return g.getPostOrder();
	}

}
