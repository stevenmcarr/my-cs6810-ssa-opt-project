package middleEnd.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.VRInstPair;
import middleEnd.utils.VirtualRegisterSet;

public class AnticipationAnalysis extends BackwardDataFlowProblem {

	private VirtualRegisterSet emptySet;
	private VirtualRegisterSet universe;
	private HashMap<String, LinkedList<IlocInstruction>> useMap = new HashMap<String, LinkedList<IlocInstruction>>();

	public AnticipationAnalysis(Cfg g, int size) {
		emptySet = new VirtualRegisterSet(size + 1);
		universe = new VirtualRegisterSet(size + 1);
		universe.set(0, size + 1);
		for (CfgNode n : g.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			Iterator<IlocInstruction> bIter = b.iterator();
			while (bIter.hasNext()) {
				IlocInstruction i = bIter.next();
				if (i.isExpression()) {
					for (Operand op : i.getRValues()) {
						if (op instanceof VirtualRegisterOperand) {
							VirtualRegisterOperand vr = (VirtualRegisterOperand) op;
							addToUseMap(vr, i);
						}
					}
				}
			}
		}
	}

	private void addToUseMap(VirtualRegisterOperand vr, IlocInstruction i) {
		String key = vr.toString();
		if (!useMap.containsKey(key)) {
			LinkedList<IlocInstruction> l = new LinkedList<IlocInstruction>();
			l.add(i);
			useMap.put(key, l);
		} else {
			useMap.get(key).add(i);
		}
	}

	@Override
	public void initialize(Cfg g) {
		for (CfgNode n : getNodeOrder(g)) {
			BasicBlock b = (BasicBlock) n;
			VirtualRegisterSet out = universe.clone();
			VirtualRegisterSet antloc = emptySet.clone();
			VirtualRegisterSet transp = universe.clone();
			Iterator<IlocInstruction> bIter = b.iterator();
			while (bIter.hasNext()) {
				IlocInstruction inst = bIter.next();
				for (VirtualRegisterOperand op : inst.getAllLValues()) {
					if (!antloc.get(op) && transp.get(op))
						antloc.set(op);
					for (IlocInstruction i : useMap.get(op))
						for (VirtualRegisterOperand vr : i.getAllLValues())
							transp.clear(vr);

				}

			}
			outMap.put(b, out);
			genMap.put(b, antloc);
			prsvMap.put(b, transp);
			setTransferResult(b, applyTransferFunc(b));
		}
	}

	@Override
	public DataFlowSet meet(BasicBlock n) {
		VirtualRegisterSet result;
		if (n.getSuccs().size() == 0) {
			result = universe.clone();
			for (CfgEdge e : n.getSuccs()) {
				BasicBlock s = (BasicBlock) e.getSucc();
				result.and(inMap.get(s));
			}
		} else {
			result = emptySet.clone();
		}

		return result;
	}

	@Override
	public DataFlowSet applyTransferFunc(BasicBlock n) {
		VirtualRegisterSet result = emptySet.clone();
		result.or(outMap.get(n));
		result.and(prsvMap.get(n));
		result.or(genMap.get(n));
		return result;
	}

}
