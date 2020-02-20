package middleEnd.dce;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.JumpIInstruction;
import middleEnd.iloc.NopInstruction;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;

public class RemoveUnreachableCode extends OptimizationPass {

    public RemoveUnreachableCode(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {
        for (IlocRoutine rtn : routines) {
            rtn.buildCfg();
            for (CfgNode n : rtn.getCfg().getNodes())
                n.clearMarked();
            markReachableBlocks(rtn.getCfg().getEntryNode());
            removeUnreachableBlocks(rtn.getCfg());
            removeJumpIToNextBlock(rtn);
            removeInitialNop(rtn);
        }

    }

    private void removeInitialNop(IlocRoutine rtn) {
        Cfg cfg = rtn.getCfg();
        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            if (b.size() > 1) {
                IlocInstruction inst = b.getFirstInst();
                if (inst instanceof NopInstruction && inst.getLabel() != null) {
                    IlocInstruction nextI = b.getNextInst(inst);
                    if (nextI != null && nextI.getLabel() == null) {
                        nextI.setLabel(inst.getLabel());
                        inst.setLabel(null);
                        b.removeInst(inst);
                    }
                }
            }
        }
    }

    private void removeJumpIToNextBlock(IlocRoutine rtn) {
        Cfg cfg = rtn.getCfg();
        for (CfgNode n : cfg.getNodes()) {
            BasicBlock b = (BasicBlock) n;
            if (b.size() > 0) {
                IlocInstruction inst = b.getLastInst();
                if (inst.getLabel() == null && inst instanceof JumpIInstruction) {
                    JumpIInstruction jmp = (JumpIInstruction) inst;
                    IlocInstruction nextI = rtn.getNextInstruction(inst);
                    if (nextI != null && nextI.getLabel() != null && jmp.getTargetLabel().equals(nextI.getLabel())) {
                        b.removeInst(jmp);
                    }
                }
            }
        }
    }

    private void removeUnreachableBlocks(Cfg g) {
        for (CfgNode n : g.getNodes())
            if (!n.isMarked())
                ((BasicBlock) n).removeAllInstructions();
    }

    private void markReachableBlocks(CfgNode n) {
        n.setMarked();
        for (CfgEdge e : n.getSuccs()) {
            if (!e.getSucc().isMarked())
                markReachableBlocks(e.getSucc());
        }
    }

}