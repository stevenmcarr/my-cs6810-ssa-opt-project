/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Cmp_leInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Cmp_leInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	public Cmp_leInstruction() {
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "cmp_LE";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "cmp_LE" + src1.toString() + src2.toString();
	}

	protected int getOperandType(Operand operand) {
		return Operand.INTEGER_TYPE;
	}

	@Override
	public IlocInstruction deepCopy() {
		Cmp_leInstruction inst = new Cmp_leInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(Cmp_leInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
