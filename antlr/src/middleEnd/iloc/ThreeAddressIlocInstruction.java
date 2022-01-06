package middleEnd.iloc;

import java.util.Hashtable;

import javax.lang.model.SourceVersion;

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
public abstract class ThreeAddressIlocInstruction extends IlocInstruction {
  Operand source1;
  Operand source2;
  Operand dest;

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + source1.toString() + ", " + source2.toString() + " => " + dest.toString());
    return rep;
  }

  public Operand getLeftOperand() {
    return source1;
  }

  public Operand getRightOperand() {
    return source2;
  }

  public Operand getDestination() {
    return dest;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    setType(typeMap, source1);
    setType(typeMap, source2);
    setType(typeMap, dest);
  }

  public boolean operandIsLValue(Operand operand) {
    return operand == dest;
  }

  public boolean operandIsRValue(Operand operand) {
    return (operand == source1 || operand == source2);
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
    if (index == 0)
      source1 = operand;
    else if (index == 1)
      source2 = operand;
    rValues.set(index, operand);
  }

  public void replaceLValue(Operand operand) {
    dest = operand;
    lValue = (VirtualRegisterOperand) operand;
  }

  public boolean isExpression() {
    return true;
  }

  @Override
  public boolean isNecessary() {
    return false;
  }

  protected void assignLRToRValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {
    if (vr == source1)
      source1 = lro;
    else if (vr == source2)
      source2 = lro;
  }

  protected void assignLRToLValue(VirtualRegisterOperand vr, LiveRangeOperand lro) {
    if (dest == vr) {
      dest = lro;
      lValue = lro;
    }
  }

  protected void copyInstanceVars(ThreeAddressIlocInstruction inst) {
    inst.dest = dest.deepCopy();
    inst.source1 = source1.deepCopy();
    inst.source2 = source2.deepCopy();
    super.copyInstanceVars(inst);
  }
}
