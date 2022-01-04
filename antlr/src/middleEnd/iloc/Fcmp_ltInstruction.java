/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class Fcmp_ltInstruction extends ThreeAddressIlocInstruction {

	/**
	 * 
	 */
	public Fcmp_ltInstruction(VirtualRegisterOperand source1, VirtualRegisterOperand source2,
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
		return "fcmp_LT";
	}

}
