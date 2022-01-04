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

}
