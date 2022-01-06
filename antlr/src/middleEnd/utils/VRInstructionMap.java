package middleEnd.utils;

import java.util.HashMap;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.VirtualRegisterOperand;

public class VRInstructionMap extends HashMap<Integer, IlocInstruction> {

	public VRInstructionMap() {
		super();
	}

	public IlocInstruction get(VirtualRegisterOperand vr) {
		return super.get(vr.getRegisterId());
	}

	public boolean containsKey(VirtualRegisterOperand vr) {
		return super.containsKey(vr.getRegisterId());
	}

	public void put(VirtualRegisterOperand vr, IlocInstruction i) {
		super.put(vr.getRegisterId(), i);
	}
}
