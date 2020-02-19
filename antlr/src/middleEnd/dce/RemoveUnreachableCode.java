package middleEnd.dce;

import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgEdge;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;

public class RemoveUnreachableCode extends OptimizationPass {

    @Override
    protected void optimizeCode() {
        for (IlocRoutine rtn : routines) {
            for (CfgNode n : rtn.getCfg().getNodes())
                n.clearMarked();
            markReachableBlocks(rtn.getCfg().getEntryNode());
            removeUnreachableBlocks(rtn.getCfg());
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