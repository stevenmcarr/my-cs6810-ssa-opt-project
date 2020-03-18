package middleEnd.iloc;

import java.util.LinkedList;

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
public class VirtualRegisterOperand extends Operand {

  public static final int FP_REG = 0;
  public static final int SP_REG = 1;
  public static final int INT_RET_REG = 2;
  public static final int FLOAT_RET_REG = 3;
  public static final int FREE_REG = 4;
  public static int maxVirtualRegister = 4;
  
  private LinkedList<IlocInstruction> defs = new LinkedList<IlocInstruction>();
  private LinkedList<IlocInstruction> uses = new LinkedList<IlocInstruction>();

  protected int registerId;
  private boolean visited = false;

  public VirtualRegisterOperand(int registerId) {
    this.registerId = registerId;
    if (registerId > maxVirtualRegister)
      maxVirtualRegister = registerId;
  }

  public VirtualRegisterOperand copy() {
    return new VirtualRegisterOperand(registerId);
  }

  public int getRegisterId() {
    return registerId;
  }

  public String toString() {
    return "%vr" + registerId;
  }

  public boolean hasBeenVisited() {
    return visited;
  }

  public void setVisited() {
    visited = true;
  }

  public void clearVisited() {
    visited = false;
  }

  public void addUse(IlocInstruction i) {
    uses.add(i);
  }

  public void addDef(IlocInstruction i) {
    defs.add(i);
  }

  public LinkedList<IlocInstruction> getUses() {
    return uses;
  }

  public LinkedList<IlocInstruction> getDefs() {
    return defs;
  }

  public void copyDefsUsesToVR(VirtualRegisterOperand vr) {
    for (IlocInstruction inst : defs)
      vr.addDef(inst);
    for (IlocInstruction inst : uses)
      vr.addUse(inst);
  }

  public VirtualRegisterOperand deepCopy() {
    VirtualRegisterOperand vr = new VirtualRegisterOperand(registerId);
    copyDefsUsesToVR(vr);
    return vr;
  }
}
