package middleEnd.utils;

import middleEnd.iloc.IlocInstruction;

public class BasicBlockInstructionsIterator {

    private BasicBlock thisBasicBlock;
    private IlocInstruction instPtr;
    private boolean reverse = false;

    public BasicBlockInstructionsIterator(BasicBlock b, boolean reverse) {
        thisBasicBlock = b;
        instPtr = null;
        this.reverse = reverse;
    }

    public BasicBlockInstructionsIterator(BasicBlock b) {
        thisBasicBlock = b;
        instPtr = null;
    }

    public boolean hasNext() {
        if (instPtr == null)
            return thisBasicBlock.getFirstInst() != null;
        else
            return instPtr != thisBasicBlock.getLastInst();
    }

    public IlocInstruction next() {
        if (instPtr == null)
            instPtr = thisBasicBlock.getFirstInst();
        else {
            instPtr = instPtr.getNextInst();
            if (instPtr != null && instPtr.getBlock() != thisBasicBlock)
                instPtr = null;
        }

        return instPtr;
    }

    public boolean hasPrevious() {
        if (instPtr == null)
            return thisBasicBlock.getLastInst() != null;
        else
            return instPtr != thisBasicBlock.getFirstInst();
    }

    public IlocInstruction previous() {
        if (instPtr == null)
            instPtr = thisBasicBlock.getLastInst();
        else {
            instPtr = instPtr.getPrevInst();
            if (instPtr != null && instPtr.getBlock() != thisBasicBlock)
                instPtr = null;
        }

        return instPtr;
    }

    public int nextIndex() {
        return 0;
    }

    public int previousIndex() {
        return 0;
    }

    public void remove(OptimizationPass p) {
        if (instPtr == null)
            return;
        IlocInstruction remInst = instPtr;
        if (reverse)
            instPtr = instPtr.getNextInst();
        else
            instPtr = instPtr.getPrevInst();
        p.removeInst(remInst);
    }

}