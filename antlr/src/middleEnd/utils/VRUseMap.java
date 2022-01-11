package middleEnd.utils;

import java.util.HashMap;

import middleEnd.iloc.VirtualRegisterOperand;

public class VRUseMap extends HashMap<Integer, VirtualRegisterSet> {
	public VRUseMap() {
		super();
	}

	public VirtualRegisterSet get(VirtualRegisterOperand vr) {
		return super.get(vr.getRegisterId());
	}

	public boolean containsKey(VirtualRegisterOperand vr) {
		return super.containsKey(vr.getRegisterId());
	}

	public void put(VirtualRegisterOperand vr, VirtualRegisterSet i) {
		super.put(vr.getRegisterId(), i);
	}

	public void putVR(VirtualRegisterOperand vr1, VirtualRegisterOperand vr2) {
		super.get(vr1.getRegisterId()).set(vr2.getRegisterId());
	}
}
