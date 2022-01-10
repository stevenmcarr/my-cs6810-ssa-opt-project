package middleEnd.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.VirtualRegisterSet;

public class AvaliableExpressionsAnalysis extends ForwardDataflowProblem {

	protected VirtualRegisterSet emptySet;
	protected VirtualRegisterSet universe;
	private HashMap<String, LinkedList<IlocInstruction>> useMap = new HashMap<String, LinkedList<IlocInstruction>>();

	public AvaliableExpressionsAnalysis(Cfg g, int size) {
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
			VirtualRegisterSet in;
			if (n.getPreds().isEmpty())
				in = emptySet.clone();
			else
				in = universe.clone();
			VirtualRegisterSet gen = emptySet.clone();
			VirtualRegisterSet prsv = universe.clone();
			VirtualRegisterSet killed = emptySet.clone();
			ListIterator<IlocInstruction> bIter = b.reverseIterator();
			while (bIter.hasPrevious()) {
				IlocInstruction inst = bIter.previous();
				boolean operandsNotKilled = true;
				for (Operand op : inst.getRValues())
					if (op instanceof VirtualRegisterOperand
							&& killed.get((VirtualRegisterOperand) op)) {
						operandsNotKilled = false;
						break;
					}

				for (VirtualRegisterOperand op : inst.getAllLValues()) {
					if (operandsNotKilled)
						gen.set(op);
					killed.set(op);
					for (IlocInstruction i : useMap.get(op.toString()))
						for (VirtualRegisterOperand vr : i.getAllLValues())
							prsv.clear(vr);

				}
			}
			inMap.put(b, in);
			genMap.put(b, gen);
			prsvMap.put(b, prsv);
			setTransferResult(b, applyTransferFunc(b));
		}
	}

	@Override
	public DataFlowSet meet(BasicBlock n) {
		VirtualRegisterSet result = universe.clone();
		for (CfgEdge e : n.getPreds()) {
			BasicBlock s = (BasicBlock) e.getPred();
			result.or(outMap.get(s));
		}
		return result;
	}

	@Override
	public DataFlowSet applyTransferFunc(BasicBlock n) {
		VirtualRegisterSet result = emptySet.clone();
		result.or(inMap.get(n));
		result.and(prsvMap.get(n));
		result.or(genMap.get(n));
		return result;
	}

}
