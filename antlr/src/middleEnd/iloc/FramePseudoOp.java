package middleEnd.iloc;

import java.util.*;

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
public class FramePseudoOp extends PseudoOpInstruction {
  String name;
  int localSize;
  Vector<Operand> parameters;

  public FramePseudoOp(String name, int localSize, Vector<Operand> parameters) {
    this.name = name;
    this.localSize = localSize;
    this.parameters = parameters;
    this.lValues = new Vector<VirtualRegisterOperand>();
    this.lValues.add(new VirtualRegisterOperand(VirtualRegisterOperand.FP_REG));
    this.lValues.add(new VirtualRegisterOperand(VirtualRegisterOperand.SP_REG));
    for (Operand op : parameters) {
      if (op instanceof VirtualRegisterOperand)
        lValues.add((VirtualRegisterOperand) op);
    }
  }

  public FramePseudoOp() {
  }

  /**
   * getOpcode
   *
   * @return String
   * @todo Implement this middleEnd.IlocInstruction method
   */
  public String getOpcode() {
    return ".frame";
  }

  public String getStringRep() {
    String rep = "";
    if (label != null)
      rep = label + ":";

    rep += ("\t" + getOpcode() + "\t" + name + ", " + localSize);

    for (int i = 0; i < parameters.size(); i++) {
      VirtualRegisterOperand reg = (VirtualRegisterOperand) parameters.elementAt(i);
      rep += (", " + reg.toString());
    }

    return rep;
  }

  public String getName() {
    return name;
  }

  public int getLocalSize() {
    return localSize;
  }

  public Vector<Operand> getParameters() {
    return parameters;
  }

  public void setOperandTypes(Hashtable<String, Integer> typeMap) {
    for (int i = 0; i < parameters.size(); i++) {
      VirtualRegisterOperand vr = (VirtualRegisterOperand) parameters.elementAt(i);
      setType(typeMap, vr);
    }
  }

  protected int getOperandType(Operand operand) {
    return Operand.INTEGER_TYPE;
  }

  public void replaceOperandAtIndex(int index, Operand operand) {
    rValues.set(index, operand);
    int rValIndex = parameters.indexOf(operand);
    if (rValIndex >= 0)
      parameters.set(rValIndex, operand);
  }

  @Override
  public void replaceLValue(Operand operand) {
    replaceLValue(operand, 0);
  }

  public void replaceLValue(Operand op, int index) {
    lValues.set(index, (VirtualRegisterOperand) op);
    if (index > 1) { // the first 2 lvalues are vr0 and vr1, the rest are paramenters
      parameters.set(index - 2, op);
    }
  }

  public void updateFrameSize(int frameSize) {
    localSize = frameSize;
  }

  @Override
  public IlocInstruction deepCopy() {
    FramePseudoOp inst = new FramePseudoOp();
    copyInstanceVars(inst);
    return inst;
  }

  protected void copyInstanceVars(FramePseudoOp inst) {
    inst.name = new String(name);
    inst.localSize = localSize;
    inst.parameters = new Vector<Operand>();
    for (Operand op : parameters) {
      Operand newOp = op.deepCopy();
      inst.parameters.add(newOp);
    }

    super.copyInstanceVars(inst);
  }
}
