/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Cmp_geInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Cmp_geInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	public Cmp_geInstruction() {
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "cmp_GE";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "cmp_GE" + src1.toString() + src2.toString();
	}

	protected int getOperandType(Operand operand) {
		return Operand.INTEGER_TYPE;
	}

	@Override
	public IlocInstruction deepCopy() {
		Cmp_geInstruction inst = new Cmp_geInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(Cmp_geInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
