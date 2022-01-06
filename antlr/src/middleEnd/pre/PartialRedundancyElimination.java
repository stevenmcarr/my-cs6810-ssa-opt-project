package middleEnd.pre;

import java.util.List;

import middleEnd.dfa.AnticipationAnalysis;
import middleEnd.dfa.AvaliableExpressionsAnalysis;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.BasicBlockDFMap;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgEdgeDFMap;
import middleEnd.utils.CfgNode;
import middleEnd.utils.DataFlowSet;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;
import middleEnd.utils.VRInstructionMap;
import middleEnd.utils.VirtualRegisterSet;

public class PartialRedundancyElimination extends OptimizationPass {

	private VRInstructionMap vrInstMap = new VRInstructionMap();
	private CfgEdgeDFMap earliestMap = new CfgEdgeDFMap();
	private CfgEdgeDFMap laterMap = new CfgEdgeDFMap();
	private CfgEdgeDFMap insertMap = new CfgEdgeDFMap();
	private BasicBlockDFMap laterinMap = new BasicBlockDFMap();
	private BasicBlockDFMap deleteMap = new BasicBlockDFMap();
	private VirtualRegisterSet emptySet;
	private VirtualRegisterSet universe;

	public PartialRedundancyElimination(String prevPassA, String passA) {
		super(prevPassA, passA);
		emptySet = new VirtualRegisterSet(maxVirtualRegister + 1);
		universe = new VirtualRegisterSet(maxVirtualRegister + 1);
		universe.set(0, maxVirtualRegister + 1);
	}

	@Override
	protected void optimizeCode() {
		for (IlocRoutine rtn : routines) {
			IlocInstruction i;
			for (i = rtn.getFirstInst(); i != null; i = rtn.getNextInstruction(i)) {
				for (VirtualRegisterOperand vr : i.getAllLValues())
					if (!vrInstMap.containsKey(vr))
						vrInstMap.put(vr, i);
			}
			rtn.buildCfg();
			for (CfgNode n : rtn.getCfg().getNodes())
				n.clearMarked();
			AnticipationAnalysis anticpAnalysis = new AnticipationAnalysis(rtn.getCfg(),
					maxVirtualRegister);
			anticpAnalysis.computeDFResult(rtn.getCfg());
			AvaliableExpressionsAnalysis aexpr = new AvaliableExpressionsAnalysis(rtn.getCfg(),
					maxVirtualRegister);
			aexpr.computeDFResult(rtn.getCfg());
			computeEarliest(rtn.getCfg(), anticpAnalysis, aexpr);
			computeLater(rtn.getCfg(), anticpAnalysis);
			computeInsert(rtn.getCfg());
			insertInstructions(rtn.getCfg());
			computeDelete(rtn.getCfg(), anticpAnalysis);
			deleteInstructions(rtn.getCfg());
		}
	}

	private void deleteInstructions(Cfg cfg) {
	}

	private void insertInstructions(Cfg cfg) {
		for (CfgNode n : cfg.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			VirtualRegisterSet insertInBlock = universe.clone();
			for (CfgEdge e : b.getPreds()) {
				VirtualRegisterSet insertin = (VirtualRegisterSet) insertMap.get(e).clone();
				insertInBlock.and(insertin);
			}

			insertAtStartBlock(b, insertInBlock);

			for (CfgEdge e : b.getPreds()) {
				VirtualRegisterSet insertEdge = (VirtualRegisterSet) insertMap.get(e).clone();
				VirtualRegisterSet remove = insertInBlock.clone();
				remove.flip(0, remove.size());
				insertEdge.and(remove);
				if (e.getSucc().getPreds().size() == 1) {
					insertAtEndBlock(b, insertEdge);
				} else if (e.getPred().getSuccs().size() == 1) {
					insertAtStartBlock(b, insertEdge);
				} else {
					insertOnEdge(b, insertEdge);
				}
			}

		}
	}

	private void insertOnEdge(BasicBlock b, VirtualRegisterSet insertEdge) {
	}

	private void insertAtEndBlock(BasicBlock b, VirtualRegisterSet insertEdge) {
	}

	private void insertAtStartBlock(BasicBlock b, VirtualRegisterSet insertInBlock) {
		IlocInstruction f = b.getFirstInst();
		int index = -1;
		while ((index = insertInBlock.nextSetBit(index + 1)) != -1) {
			IlocInstruction inst = vrInstMap.get(new VirtualRegisterOperand(index));
			// clone the instruction
			// insert an instruction before f
		}
	}

	private void computeDelete(Cfg cfg, AnticipationAnalysis anticpAnalysis) {
		for (CfgNode n : cfg.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			VirtualRegisterSet delete = emptySet.clone();
			if (!b.getPreds().isEmpty()) {
				delete = (VirtualRegisterSet) anticpAnalysis.getGenMap().get(b).clone();
				VirtualRegisterSet laterin = (VirtualRegisterSet) laterinMap.get(b).clone();
				laterin.flip(0, laterin.size());
				delete.and(laterin);
				deleteMap.put(b, delete);
			}
		}
	}

	private void computeInsert(Cfg cfg) {

		for (CfgNode n : cfg.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			for (CfgEdge e : b.getPreds()) {
				if (!insertMap.containsKey(e)) {
					VirtualRegisterSet insert = (VirtualRegisterSet) laterMap.get(e).clone();
					VirtualRegisterSet laterin = (VirtualRegisterSet) laterinMap.get(b).clone();
					laterin.flip(0, laterin.size());
					insert.and(laterin);
					insertMap.put(e, insert);
				}
			}
		}
	}

	private void computeEarliest(Cfg cfg, AnticipationAnalysis anticpAnalysis,
			AvaliableExpressionsAnalysis aexpr) {
		for (CfgNode n : cfg.getNodes()) {
			BasicBlock b = (BasicBlock) n;
			for (CfgEdge e : b.getPreds()) {
				if (!earliestMap.containsKey(e)) {
					VirtualRegisterSet earliest = new VirtualRegisterSet();
					earliest = (VirtualRegisterSet) anticpAnalysis.getInMap()
							.get((BasicBlock) e.getSucc()).clone();
					VirtualRegisterSet avOut = (VirtualRegisterSet) aexpr.getOutMap()
							.get((BasicBlock) e.getPred()).clone();
					avOut.flip(0, avOut.size());
					earliest.and(avOut);
					if (!e.getPred().getPreds().isEmpty()) {
						VirtualRegisterSet transp = (VirtualRegisterSet) anticpAnalysis
								.getPrsvMap().get((BasicBlock) e.getPred()).clone();
						transp.flip(0, transp.size());
						VirtualRegisterSet antOut = (VirtualRegisterSet) anticpAnalysis
								.getOutMap().get((BasicBlock) e.getPred()).clone();
						antOut.flip(0, antOut.size());
						transp.or(antOut);
						earliest.and(transp);
					}
					earliestMap.put(e, earliest);
				}
			}
		}
	}

	private void computeLater(Cfg cfg, AnticipationAnalysis anticpAnalysis) {

		boolean changed;
		do {
			changed = false;
			for (CfgNode n : getNodeOrder(cfg)) {
				BasicBlock b = (BasicBlock) n;
				DataFlowSet meetResult = meet(b);
				if (!meetResult.equals(getCurrentMeetResult(b))) {
					changed = true;
					setMeetResult(b, meetResult);
					for (CfgEdge e : b.getPreds()) {
						DataFlowSet transferResult = applyTransferFunc(e, anticpAnalysis);
						setTransferResult(e, transferResult);
					}
				}
			}

		} while (changed);
	}

	private DataFlowSet applyTransferFunc(CfgEdge e, AnticipationAnalysis anticpAnalysis) {
		VirtualRegisterSet later = (VirtualRegisterSet) laterinMap.get((BasicBlock) e.getPred()).clone();
		VirtualRegisterSet antloc = (VirtualRegisterSet) anticpAnalysis.getGenMap()
				.get((BasicBlock) e.getPred()).clone();

		antloc.flip(0, antloc.size());
		later.and(antloc);

		VirtualRegisterSet earliest = (VirtualRegisterSet) earliestMap.get(e);

		later.or(earliest);

		return later;
	}

	private DataFlowSet meet(BasicBlock b) {
		VirtualRegisterSet laterin = emptySet.clone();
		if (!b.getPreds().isEmpty()) {
			laterin = universe.clone();
			for (CfgEdge e : b.getPreds()) {
				laterin.and(laterMap.get(e));
			}
		}
		return laterin;
	}

	private DataFlowSet getCurrentMeetResult(BasicBlock n) {
		return laterinMap.get(n);
	}

	private void setMeetResult(BasicBlock n, DataFlowSet vrs) {
		laterinMap.put(n, vrs);
	}

	private void setTransferResult(CfgEdge e, DataFlowSet vrs) {
		laterMap.put(e, vrs);
	}

	private List<CfgNode> getNodeOrder(Cfg g) {
		return g.getPreOrder();
	}
}
