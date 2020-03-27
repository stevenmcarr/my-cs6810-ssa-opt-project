package middleEnd.utils;

import java.util.HashMap;

import middleEnd.iloc.IlocInstruction;

public class IlocInstructionSet extends DataFlowSet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HashMap<Integer, IlocInstruction> instMap = new HashMap<Integer, IlocInstruction>();

    public IlocInstructionSet() {
        super();
    }

    public IlocInstructionSet(int size) {
        super(size);
    }

    public IlocInstructionSet(IlocInstructionSet ds) {
        setInstMap(instMap);
        or(ds);
    }

    public IlocInstructionSet(HashMap<Integer, IlocInstruction> im) {
        instMap = im;
    }

    public IlocInstructionSet clone() {
        return (IlocInstructionSet) super.clone();
    }

    public void setInstMap(HashMap<Integer, IlocInstruction> im) {
        instMap = im;
    }

    public IlocInstruction getIlocInstruction(int index) {
        if (get(index))
            return instMap.get(index);
        else
            return null;
    }

    public void setAll() {
        for (Integer n : instMap.keySet())
            set(n);
    }

    public void set(IlocInstruction i) {
        set(i.getInstId());
    }

    public boolean get(IlocInstruction i) {
        return get(i.getInstId());
    }

    public void clear(IlocInstruction i) {
        clear(i.getInstId());
    }

    public void flip(IlocInstruction i) {
        flip(i.getInstId());
    }
}
