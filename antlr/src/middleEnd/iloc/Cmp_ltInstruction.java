/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Cmp_ltInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Cmp_ltInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	public Cmp_ltInstruction() {
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "cmp_LT";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "cmp_LT" + src1.toString() + src2.toString();
	}

	protected int getOperandType(Operand operand) {
		return Operand.INTEGER_TYPE;
	}

	@Override
	public IlocInstruction deepCopy() {
		Cmp_ltInstruction inst = new Cmp_ltInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(Cmp_ltInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
