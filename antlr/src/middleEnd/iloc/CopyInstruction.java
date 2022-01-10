package middleEnd.iloc;

/**
 * <p>
 * Title: Nolife Compiler
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author Steve Carr
 * @version 1.0
 */
public abstract class CopyInstruction extends TwoAddressIlocInstruction {

    public boolean isExpression() {
        return false;
    }

    protected void copyInstanceVars(CopyInstruction inst) {
        super.copyInstanceVars(inst);
    }
}
