package middleEnd.iloc;

import middleEnd.utils.*;

import java.io.PrintWriter;
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
public abstract class IlocInstruction {

  private static int numInst = 0;

  protected String label = null;

  protected VirtualRegisterOperand lValue = null; // a VirtualRegisterOperand
  protected Vector<VirtualRegisterOperand> lValues = new Vector<VirtualRegisterOperand>();
  protected Vector<Operand> rValues = new Vector<Operand>(); // only VirtualRegisterOperands

  protected int instId;

  public IlocInstruction() {
    instId = numInst++;
  }

  public int getInstId() {
    return instId;
  }

  public abstract String getOpcode();

  public void emit(PrintWriter pw) {
    pw.println(getStringRep());
  }

  public abstract String getStringRep();

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

}
