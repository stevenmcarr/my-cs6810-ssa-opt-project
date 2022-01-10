/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Fcmp_gtInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Fcmp_gtInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	public Fcmp_gtInstruction() {
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "fcmp_GT";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "fcmp_GT" + src1.toString() + src2.toString();
	}

	protected int getOperandType(Operand operand) {
		if (dest == operand)
			return Operand.INTEGER_TYPE;
		else
			return Operand.FLOAT_TYPE;
	}

	@Override
	public IlocInstruction deepCopy() {
		Fcmp_gtInstruction inst = new Fcmp_gtInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(Fcmp_gtInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
