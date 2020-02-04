package middleEnd.dbre;

import java.util.HashMap;
import java.util.LinkedList;

import middleEnd.iloc.SSAVROperand;

public class AvailTableStack extends LinkedList<HashMap<String, SSAVROperand>> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AvailTableStack() {
        super();
    }

    public void startBlock() {
        push(new HashMap<String, SSAVROperand>());
    }

    public void endBlock() {
        pop();
    }

    public SSAVROperand get(String key) {
        SSAVROperand ssavr = null;
        for (HashMap<String, SSAVROperand> map : this) {
            if (map.containsKey(key)) {
                ssavr = map.get(key);
                break;
            }
        }

        return ssavr;
    }

    public void put(String key, SSAVROperand svr) {
        HashMap<String, SSAVROperand> map = getFirst();
        map.put(key, svr);
    }
}