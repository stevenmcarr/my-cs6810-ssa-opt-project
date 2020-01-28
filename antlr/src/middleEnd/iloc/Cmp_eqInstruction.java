/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Cmp_eqInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Cmp_eqInstruction(VirtualRegisterOperand source1,
			VirtualRegisterOperand source2,
			VirtualRegisterOperand dest) {
		this.source1 = source1;
		this.source2 = source2;
		this.dest = dest;
		lValue = dest;
		rValues.add(source1);
		rValues.add(source2);
	}

	/**
	 * getOpcode
	 *
	 * @return String
	 * @todo Implement this middleEnd.IlocInstruction method
	 */
	public String getOpcode() {
		return "cmp_EQ";
	}

	public static String getHash(VirtualRegisterOperand src1,
			VirtualRegisterOperand src2) {
		return "cmp_EQ"+src1.toString()+src2.toString();
	}

	protected int getOperandType(Operand operand) {
		return Operand.INTEGER_TYPE;
	}

}
