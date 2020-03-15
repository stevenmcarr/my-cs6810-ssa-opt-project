package backend.ra;

import java.util.List;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.DUUDChains;
import middleEnd.utils.DefinitionSet;
import middleEnd.utils.IlocInstructionSet;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.OptimizationPass;

public class ChaitinBriggs extends OptimizationPass {

    public ChaitinBriggs(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void optimizeCode() {
        for (IlocRoutine ir : getRoutines()) {
            DUUDChains chains = (new DUUDChains()).addCfg(ir.getCfg());
            chains.build();
            DefinitionSet ds = chains.getAllDefintions();
            for (String vr : ds.getVrsDefined()) {
                IlocInstructionSet iis = ds.getInstructionSet(vr);
                for (int index = iis.nextSetBit(0); index >= 0; index = iis.nextSetBit(index + 1)) {
                    IlocInstruction inst = iis.getIlocInstruction(index);
                    for (VirtualRegisterOperand vro : inst.getAllLValues())
                        if (!vro.hasBeenVisited()) {

                            // build a list of VirtualRegister/IlocInstruction pairs to construct a live range
                           // go through the list and replace all VRs with LiveRangeOperands

                        }
                }
            }
        }
    }

    private List<VRInstPair> buildLiveRange(VirtualRegisterOperand vro) {
        return null;
    }
}