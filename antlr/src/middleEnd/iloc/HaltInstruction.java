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

}
