package middleEnd.iloc;

import java.util.Hashtable;
import java.util.Vector;

public class PhiNode extends VariableAddressIlocInstruction {

    public PhiNode(Vector<Operand> rValues, VirtualRegisterOperand lValue) {
        this.rValues = new Vector<Operand>(rValues);
        this.lValue = lValue;
        operands = new Vector<Operand>(rValues);
    }

    @Override
    public String getOpcode() {
        return "phi";
    }

    public void setOperandTypes(Hashtable<String, Integer> typeMap) {
        for (int i = 1; i < operands.size(); i++) {
            VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(i);
            Integer typeVal = (Integer) typeMap.get(vr.toString());
            vr.setType(typeVal.intValue());
        }
        setType(typeMap, lValue);
    }

    public int getOperandType(Operand operand) {
        int index = operands.indexOf(operand);

        if (index != -1) {
            VirtualRegisterOperand vr = (VirtualRegisterOperand) operands.elementAt(index);
            return vr.getType();
        } else
            return Operand.INTEGER_TYPE;
    }

    protected String getStringRepSpecific() {
        return ("\t => " + lValue.toString());
    }

    @Override
    public boolean isExpression() {
        return false;
    }

}