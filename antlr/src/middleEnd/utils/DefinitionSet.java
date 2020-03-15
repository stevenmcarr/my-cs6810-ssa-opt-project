package middleEnd.utils;

import java.util.HashMap;
import java.util.LinkedList;

import middleEnd.iloc.IlocInstruction;

public class DefinitionSet extends DataFlowSet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static int definitionId = 0;

    private HashMap<String, Integer> defMap = new HashMap<String, Integer>();
    private HashMap<String, IlocInstructionSet> instMap = new HashMap<String, IlocInstructionSet>();
    private LinkedList<String> vrsDefined = new LinkedList<String>();

    public DefinitionSet() {
        super();
    }

    public DefinitionSet(int size) {
        super(size);
    }

    public DefinitionSet(DefinitionSet ds) {
        setDefMap(defMap);
        setInstMap(instMap);
        setVrsDefined(vrsDefined);
        or(ds);
    }

    public DefinitionSet clone() {
        return new DefinitionSet(this);
    }

    public int getNumDefinitions() {
        return definitionId;
    }

    public void setDefMap(HashMap<String, Integer> im) {
        defMap = im;
    }

    public void setInstMap(HashMap<String, IlocInstructionSet> im) {
        instMap = im;
    }

    private void setVrsDefined(LinkedList<String> vrsDefined2) {
        vrsDefined = vrsDefined2;
    }

    private String makeKey(String vr, IlocInstruction i) {
        return vr + "$" + i.getInstId();
    }

    public void set(String vr, IlocInstruction i) {
        String key = makeKey(vr, i);
        if (!defMap.containsKey(key)) {
            defMap.put(key, definitionId);
        }
        set(defMap.get(key));

        if (!instMap.containsKey(vr)) {
            instMap.put(vr, new IlocInstructionSet());
        }
        instMap.get(vr).set(i);
    }

    public boolean get(String vr, IlocInstruction i) {
        String key = makeKey(vr, i);
        if (!defMap.containsKey(key))
            return false;
        else
            return get(defMap.get(key));
    }

    public void clear(String vr, IlocInstruction i) {
        String key = makeKey(vr, i);
        if (!defMap.containsKey(key)) {
            defMap.put(key, definitionId++);
        }
        clear(defMap.get(key));
    }

    public void clearAll() {
        clear(0, size() - 1);
    }

    public void flip(String vr, IlocInstruction i) {
        String key = makeKey(vr, i);
        if (!defMap.containsKey(key)) {
            defMap.put(key, definitionId++);
        }
        flip(defMap.get(key));
    }

    public IlocInstructionSet getInstructionSet(String vr) {
        return instMap.get(vr);
    }

    public LinkedList<String> getVrsDefined() {
        return vrsDefined;
    }

}