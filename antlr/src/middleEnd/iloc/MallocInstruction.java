/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class MallocInstruction extends TwoAddressIlocInstruction {

	/**
	 * 
	 */
	public MallocInstruction(VirtualRegisterOperand source,
			VirtualRegisterOperand dest) {
		this.source = source;
		this.dest = dest;
	    lValue = dest;
	    rValues.add(source);
	}

	/* (non-Javadoc)
	 * @see middleEnd.iloc.IlocInstruction#getOpcode()
	 */
	@Override
	public String getOpcode() {
		return "malloc";
	}

	public static String getHash(VirtualRegisterOperand src) {
		return "malloc"+src.toString();
	}

	protected int getOperandType(Operand operand) {
		return Operand.INTEGER_TYPE;
	}

}
