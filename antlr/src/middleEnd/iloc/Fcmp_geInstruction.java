/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Fcmp_geInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Fcmp_geInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	public Fcmp_geInstruction() {
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "fcmp_GE";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "fcmp_GE" + src1.toString() + src2.toString();
	}

	protected int getOperandType(Operand operand) {
		if (dest == operand)
			return Operand.INTEGER_TYPE;
		else
			return Operand.FLOAT_TYPE;
	}

	@Override
	public IlocInstruction deepCopy() {
		Fcmp_geInstruction inst = new Fcmp_geInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(Fcmp_geInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
