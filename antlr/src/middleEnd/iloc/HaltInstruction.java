/**
 * 
 */
package middleEnd.iloc;

/**
 * @author carr
 *
 */
public class HaltInstruction extends NoAddressIlocInstruction {

	/**
	 * 
	 */
	public HaltInstruction() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#getOpcode()
	 */
	@Override
	public String getOpcode() {
		return "halt";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#getOperandType(middleEnd.iloc.Operand)
	 */
	@Override
	protected int getOperandType(Operand operand) {
		return 0;
	}

	@Override
	public boolean isExpression() {
		return false;
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public IlocInstruction deepCopy() {
		HaltInstruction inst = new HaltInstruction();
		copyInstanceVars(inst);
		return inst;
	}

	protected void copyInstanceVars(HaltInstruction inst) {
		super.copyInstanceVars(inst);
	}

}
