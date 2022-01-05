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
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.VRInstPair;
import middleEnd.utils.VirtualRegisterSet;

public class AnticipationAnalysis extends IterativeFramework {

	private VirtualRegisterSet emptySet;
	private VirtualRegisterSet universe;
	private BasicBlockDFMap inMap = new BasicBlockDFMap();
	private BasicBlockDFMap outMap = new BasicBlockDFMap();
	private BasicBlockDFMap antlocMap = new BasicBlockDFMap();
	private BasicBlockDFMap killMap = new BasicBlockDFMap();
	private HashMap<String, LinkedList<VRInstPair>> useMap = new HashMap<String, LinkedList<VRInstPair>>();
	private HashMap<Integer, VRInstPair> pairMap = new HashMap<Integer, VRInstPair>();

	public AnticipationAnalysis(Cfg g, int size) {
		emptySet = new VirtualRegisterSet(size + 1);
		universe = new VirtualRegisterSet(size + 1);
		universe.set(0, size + 1);
		for (CfgNode n : g.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			Iterator<IlocInstruction> bIter = b.iterator();
			while (bIter.hasNext()) {
				IlocInstruction i = bIter.next();
				for (Operand op : i.getRValues()) {
					if (op instanceof VirtualRegisterOperand) {
						VirtualRegisterOperand vr = (VirtualRegisterOperand) op;
						VRInstPair vrp = (new VRInstPair()).addVR(vr).addInst(i);
						addToUseMap(vr, vrp);
						pairMap.put(vrp.getPairId(), vrp);
					}
				}
			}
		}
	}

	private void addToUseMap(VirtualRegisterOperand vr, VRInstPair vrp) {
		String key = vr.toString();
		if (!useMap.containsKey(key)) {
			LinkedList<VRInstPair> l = new LinkedList<VRInstPair>();
			l.add(vrp);
			useMap.put(key, l);
		} else {
			useMap.get(key).add(vrp);
		}
	}

	private VRInstPair findPair(VirtualRegisterOperand vr, IlocInstruction i) {
		LinkedList<VRInstPair> l = useMap.get(vr.toString());
		VRInstPair pair = null;
		for (VRInstPair vrp : l) {
			if (vrp.isPairEqual(vr, i)) {
				pair = vrp;
				break;
			}
		}
		return pair;
	}

	@Override
	public void initialize(Cfg g) {
		for (CfgNode n : getNodeOrder(g)) {
			BasicBlock b = (BasicBlock) n;
			VirtualRegisterSet out = emptySet.clone();
			VirtualRegisterSet antloc = emptySet.clone();
			VirtualRegisterSet kill = emptySet.clone();
			Iterator<IlocInstruction> bIter = b.iterator();
			while (bIter.hasNext()) {
				IlocInstruction inst = bIter.next();
				Vector<VirtualRegisterOperand> lv = inst.getAllLValues();
				for (VirtualRegisterOperand op : lv) {
					if (!antloc.get(op) && !kill.get(op))
						antloc.set(op);
				}

			}
			outMap.put(b, out);
			antlocMap.put(b, antloc);
			killMap.put(b, kill);
			setTransferResult(b, applyTransferFunc(b));
		}
	}

	@Override
	public DataFlowSet meet(BasicBlock n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataFlowSet getCurrentMeetResult(BasicBlock n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMeetResult(BasicBlock n, DataFlowSet vrs) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataFlowSet applyTransferFunc(BasicBlock n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransferResult(BasicBlock n, DataFlowSet vrs) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CfgNode> getNodeOrder(Cfg g) {
		// TODO Auto-generated method stub
		return null;
	}

}
