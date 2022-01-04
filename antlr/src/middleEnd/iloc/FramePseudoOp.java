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

}
