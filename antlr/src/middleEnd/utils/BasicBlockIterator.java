package middleEnd.utils;

import java.util.ListIterator;

import middleEnd.iloc.IlocInstruction;

public class BasicBlockIterator implements ListIterator<IlocInstruction> {

    private BasicBlock thisBasicBlock;
    private IlocInstruction instPtr;
    private boolean reverse = false;

    public BasicBlockIterator(BasicBlock b, boolean reverse) {
        thisBasicBlock = b;
        instPtr = null;
        this.reverse = reverse;
    }

    public BasicBlockIterator(BasicBlock b) {
        thisBasicBlock = b;
        instPtr = null;
    }

    @Override
    public boolean hasNext() {
        if (instPtr == null)
            return thisBasicBlock.getFirstInst() != null;
        else
            return instPtr != thisBasicBlock.getLastInst().getNextInst();
    }

    @Override
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

    @Override
    public boolean hasPrevious() {
        if (instPtr == null)
            return thisBasicBlock.getLastInst() != null;
        else
            return instPtr != thisBasicBlock.getFirstInst().getPrevInst();
    }

    @Override
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

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return 0;
    }

    @Override
    public void remove() {
        if (instPtr == null)
            return;
        IlocInstruction remInst = instPtr;
        if (reverse)
            instPtr = instPtr.getNextInst();
        else
            instPtr = instPtr.getPrevInst();
        thisBasicBlock.removeInst(remInst);
    }

    @Override
    public void set(IlocInstruction e) {
    }

    @Override
    public void add(IlocInstruction e) {
    }

}