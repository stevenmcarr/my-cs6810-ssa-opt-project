package middleEnd.iloc;

import java.util.Vector;

/**
 * <p>
 * Title: CS4131 Nolife Compiler
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
 * @author not attributable
 * @version 1.0
 */
public class ModInstruction extends ThreeAddressIlocInstruction {
  public ModInstruction(VirtualRegisterOperand source1, VirtualRegisterOperand source2, VirtualRegisterOperand dest) {
    this.source1 = source1;
    this.source2 = source2;
    this.dest = dest;
    lValue = dest;
    rValues.add(source1);
    rValues.add(source2);
  }

  public String getOpcode() {
    return "mod";
  }

}
