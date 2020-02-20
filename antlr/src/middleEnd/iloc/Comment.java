/**
 * 
 */
package middleEnd.iloc;

import java.util.Hashtable;

/**
 * @author carr
 *
 */
public class Comment extends IlocInstruction {

	public String commentString;

	/**
	 * 
	 */
	public Comment(String str) {
		commentString = new String(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#emit()
	 */
	@Override
	public String getStringRep() {
		String rep = "";
		if (label != null)
			rep = label + ":";
		rep += ("# " + commentString);
		return rep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#getOpcode()
	 */
	@Override
	public String getOpcode() {
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#operandIsLValue(middleEnd.iloc.Operand)
	 */
	@Override
	public boolean operandIsLValue(Operand operand) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#operandIsRValue(middleEnd.iloc.Operand)
	 */
	@Override
	public boolean operandIsRValue(Operand operand) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see middleEnd.iloc.IlocInstruction#setOperandTypes(java.util.Hashtable)
	 */
	@Override
	public void setOperandTypes(Hashtable<String, Integer> typeHash) {

	}

	@Override
	public void replaceOperandAtIndex(int index, Operand operand) {

	}

	@Override
	public void replaceLValue(Operand operand) {

	}

	@Override
	public boolean isExpression() {
		return false;
	}

	@Override
	public boolean isNecessary() {
		return false;
	}
}
