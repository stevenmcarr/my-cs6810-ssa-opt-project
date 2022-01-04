package middleEnd.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import middleEnd.iloc.AddIInstruction;
import middleEnd.iloc.AddInstruction;
import middleEnd.iloc.AndInstruction;
import middleEnd.iloc.CallInstruction;
import middleEnd.iloc.CbrInstruction;
import middleEnd.iloc.CbrneInstruction;
import middleEnd.iloc.CompInstruction;
import middleEnd.iloc.ConstantOperand;
import middleEnd.iloc.DataSection;
import middleEnd.iloc.F2iInstruction;
import middleEnd.iloc.FaddInstruction;
import middleEnd.iloc.FloadInstruction;
import middleEnd.iloc.FloatPseudoOp;
import middleEnd.iloc.FmultInstruction;
import middleEnd.iloc.FramePseudoOp;
import middleEnd.iloc.GlobalPseudoOp;
import middleEnd.iloc.I2fInstruction;
import middleEnd.iloc.I2iInstruction;
import middleEnd.iloc.IcallInstruction;
import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.ImmediateOperand;
import middleEnd.iloc.IreadInstruction;
import middleEnd.iloc.IretInstruction;
import middleEnd.iloc.IwriteInstruction;
import middleEnd.iloc.JumpIInstruction;
import middleEnd.iloc.LabelOperand;
import middleEnd.iloc.LoadIInstruction;
import middleEnd.iloc.LoadInstruction;
import middleEnd.iloc.ModInstruction;
import middleEnd.iloc.MultIInstruction;
import middleEnd.iloc.MultInstruction;
import middleEnd.iloc.NopInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.OrInstruction;
import middleEnd.iloc.RetInstruction;
import middleEnd.iloc.StoreInstruction;
import middleEnd.iloc.StringPseudoOp;
import middleEnd.iloc.SubIInstruction;
import middleEnd.iloc.SubInstruction;
import middleEnd.iloc.SwriteInstruction;
import middleEnd.iloc.TesteqInstruction;
import middleEnd.iloc.TestgeInstruction;
import middleEnd.iloc.TestgtInstruction;
import middleEnd.iloc.TestleInstruction;
import middleEnd.iloc.TestltInstruction;
import middleEnd.iloc.TestneInstruction;
import middleEnd.iloc.TextSection;
import middleEnd.iloc.VirtualRegisterOperand;
import parser.ilocBaseVisitor;
import parser.ilocLexer;
import parser.ilocParser;
import parser.ilocParser.DataContext;
import parser.ilocParser.FrameInstructionContext;
import parser.ilocParser.IlocInstructionContext;
import parser.ilocParser.ImmediateValContext;
import parser.ilocParser.OperationContext;
import parser.ilocParser.ProcedureContext;
import parser.ilocParser.ProgramContext;
import parser.ilocParser.PseudoOpContext;
import parser.ilocParser.VirtualRegContext;

public abstract class OptimizationPass extends ilocBaseVisitor<Void> {

  protected String inputFileName;
  protected String prevPassAbbrev;
  protected String passAbbrev;

  protected LinkedList<IlocInstruction> allInstructions = new LinkedList<IlocInstruction>();
  private VirtualRegisterOperand lastVR = null;
  private ImmediateOperand lastOp = null;
  private IlocInstruction newI = null;

  protected Vector<IlocRoutine> routines = new Vector<IlocRoutine>();

  protected Hashtable<String, Integer> instHash = new Hashtable<String, Integer>();

  protected int nextVirtualRegister = VirtualRegisterOperand.FREE_REG;
  protected int maxVirtualRegister = 0;

  protected Hashtable<String, Integer> typeHash = new Hashtable<String, Integer>();

  public OptimizationPass(String prevPassA, String passA) {
    prevPassAbbrev = prevPassA;
    passAbbrev = passA;
  }

  public void emitCode(PrintWriter pw) {
    for (IlocRoutine rtn : routines)
      rtn.emitCode(pw);
  }

  public void buildRoutines() {

  }

  public Vector<IlocRoutine> getRoutines() {
    return routines;
  }

  @Override
  public Void visitProgram(ProgramContext ctx) {
    if (ctx.data() != null)
      ctx.data().accept(this);
    allInstructions.add(new TextSection());
    ctx.procedures().accept(this);
    return null;
  }

  @Override
  public Void visitData(DataContext ctx) {
    allInstructions.add(new DataSection());
    for (ParseTree t : ctx.pseudoOp())
      t.accept(this);
    return null;
  }

  @Override
  public Void visitProcedure(ProcedureContext ctx) {
    ctx.frameInstruction().accept(this);
    for (ParseTree t : ctx.ilocInstruction())
      t.accept(this);
    return null;
  }

  @Override
  public Void visitFrameInstruction(FrameInstructionContext ctx) {
    Vector<Operand> vrs = new Vector<Operand>();
    for (ParseTree t : ctx.virtualReg()) {
      t.accept(this);
      vrs.add(lastVR);
    }
    int localSize = Integer.parseInt(ctx.NUMBER().getText());
    allInstructions.add(new FramePseudoOp(ctx.LABEL().getText(), localSize, vrs));
    return null;
  }

  IlocInstruction makeAddInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new AddInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeAddIInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ImmediateOperand op = new ConstantOperand(Integer.parseInt(ctx.NUMBER().getText()));

    return new AddIInstruction(vr0, op, vr1);
  }

  IlocInstruction makeAndInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new AndInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeCallInstruction(OperationContext ctx) {

    Vector<Operand> vrs = new Vector<Operand>();
    for (ParseTree t : ctx.virtualReg()) {
      t.accept(this);
      vrs.add(lastVR);
    }
    LabelOperand name = new LabelOperand(ctx.LABEL(0).getText());

    return new CallInstruction(name, vrs);
  }

  IlocInstruction makeIcallInstruction(OperationContext ctx) {

    Vector<Operand> vrs = new Vector<Operand>();
    for (ParseTree t : ctx.virtualReg()) {
      t.accept(this);
      vrs.add(lastVR);
    }

    VirtualRegisterOperand vr = (VirtualRegisterOperand) vrs.lastElement();
    vrs.remove(vr);

    LabelOperand name = new LabelOperand(ctx.LABEL(0).getText());

    return new IcallInstruction(name, vrs, vr);
  }

  IlocInstruction makeCbrInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    LabelOperand lb = new LabelOperand(ctx.LABEL(0).getText());

    return new CbrInstruction(vr0, lb);
  }

  IlocInstruction makeCbrneInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    LabelOperand lb = new LabelOperand(ctx.LABEL(0).getText());

    return new CbrneInstruction(vr0, lb);
  }

  IlocInstruction makeCompInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new CompInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeF2iInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new F2iInstruction(vr0, vr1);
  }

  IlocInstruction makeFaddInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new FaddInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeFloadInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new FloadInstruction(vr0, vr1);
  }

  IlocInstruction makeFmultInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new FmultInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeI2fInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new I2fInstruction(vr0, vr1);
  }

  IlocInstruction makeI2iInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new I2iInstruction(vr0, vr1);
  }

  IlocInstruction makeIreadInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;

    return new IreadInstruction(vr0);
  }

  IlocInstruction makeIretInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;

    return new IretInstruction(vr0);
  }

  IlocInstruction makeIwriteInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;

    return new IwriteInstruction(vr0);
  }

  IlocInstruction makeJumpIInstruction(OperationContext ctx) {
    LabelOperand lb = new LabelOperand(ctx.LABEL(0).getText());

    return new JumpIInstruction(lb);
  }

  IlocInstruction makeLoadInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new LoadInstruction(vr0, vr1);
  }

  IlocInstruction makeLoadIInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.immediateVal().accept(this);

    return new LoadIInstruction(lastOp, vr0);
  }

  IlocInstruction makeModInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new ModInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeMultInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new MultInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeMultIInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ImmediateOperand op = new ConstantOperand(Integer.parseInt(ctx.NUMBER().getText()));

    return new MultIInstruction(vr0, op, vr1);
  }

  IlocInstruction makeOrInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new OrInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeStoreInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new StoreInstruction(vr0, vr1);
  }

  IlocInstruction makeSubInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ctx.virtualReg(2).accept(this);
    VirtualRegisterOperand vr2 = lastVR;

    return new SubInstruction(vr0, vr1, vr2);
  }

  IlocInstruction makeSubIInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;
    ImmediateOperand op = new ConstantOperand(Integer.parseInt(ctx.NUMBER().getText()));

    return new SubIInstruction(vr0, op, vr1);
  }

  IlocInstruction makeTesteqInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TesteqInstruction(vr0, vr1);
  }

  IlocInstruction makeTestneInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TestneInstruction(vr0, vr1);
  }

  IlocInstruction makeTestgtInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TestgtInstruction(vr0, vr1);
  }

  IlocInstruction makeTestgeInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TestgeInstruction(vr0, vr1);
  }

  IlocInstruction makeTestltInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TestltInstruction(vr0, vr1);
  }

  IlocInstruction makeTestleInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;
    ctx.virtualReg(1).accept(this);
    VirtualRegisterOperand vr1 = lastVR;

    return new TestleInstruction(vr0, vr1);
  }

  IlocInstruction makeSwriteInstruction(OperationContext ctx) {
    ctx.virtualReg(0).accept(this);
    VirtualRegisterOperand vr0 = lastVR;

    return new SwriteInstruction(vr0);
  }

  @Override
  public Void visitOperation(OperationContext ctx) {
    if (ctx.ADD() != null)
      newI = makeAddInstruction(ctx);
    else if (ctx.ADDI() != null)
      newI = makeAddIInstruction(ctx);
    else if (ctx.AND() != null)
      newI = makeAndInstruction(ctx);
    else if (ctx.CALL() != null)
      newI = makeCallInstruction(ctx);
    else if (ctx.CBR() != null)
      newI = makeCbrInstruction(ctx);
    else if (ctx.CBRNE() != null)
      newI = makeCbrneInstruction(ctx);
    else if (ctx.COMP() != null)
      newI = makeCompInstruction(ctx);
    else if (ctx.F2I() != null)
      newI = makeF2iInstruction(ctx);
    else if (ctx.FADD() != null)
      newI = makeFaddInstruction(ctx);
    else if (ctx.FLOAD() != null)
      newI = makeFloadInstruction(ctx);
    else if (ctx.FMULT() != null)
      newI = makeFmultInstruction(ctx);
    else if (ctx.I2F() != null)
      newI = makeI2fInstruction(ctx);
    else if (ctx.I2I() != null)
      newI = makeI2iInstruction(ctx);
    else if (ctx.ICALL() != null)
      newI = makeIcallInstruction(ctx);
    else if (ctx.IREAD() != null)
      newI = makeIreadInstruction(ctx);
    else if (ctx.IRET() != null)
      newI = makeIretInstruction(ctx);
    else if (ctx.IWRITE() != null)
      newI = makeIwriteInstruction(ctx);
    else if (ctx.JUMPI() != null)
      newI = makeJumpIInstruction(ctx);
    else if (ctx.LOAD() != null)
      newI = makeLoadInstruction(ctx);
    else if (ctx.LOADI() != null)
      newI = makeLoadIInstruction(ctx);
    else if (ctx.MOD() != null)
      newI = makeModInstruction(ctx);
    else if (ctx.MULT() != null)
      newI = makeMultInstruction(ctx);
    else if (ctx.MULTI() != null)
      newI = makeMultIInstruction(ctx);
    else if (ctx.NOP() != null)
      newI = new NopInstruction();
    else if (ctx.OR() != null)
      newI = makeOrInstruction(ctx);
    else if (ctx.RET() != null)
      newI = new RetInstruction();
    else if (ctx.STORE() != null)
      newI = makeStoreInstruction(ctx);
    else if (ctx.SUB() != null)
      newI = makeSubInstruction(ctx);
    else if (ctx.SUBI() != null)
      newI = makeSubIInstruction(ctx);
    else if (ctx.TESTEQ() != null)
      newI = makeTesteqInstruction(ctx);
    else if (ctx.TESTNE() != null)
      newI = makeTestneInstruction(ctx);
    else if (ctx.TESTGT() != null)
      newI = makeTestgtInstruction(ctx);
    else if (ctx.TESTGE() != null)
      newI = makeTestgeInstruction(ctx);
    else if (ctx.TESTLT() != null)
      newI = makeTestltInstruction(ctx);
    else if (ctx.TESTLE() != null)
      newI = makeTestleInstruction(ctx);
    else if (ctx.SWRITE() != null)
      newI = makeSwriteInstruction(ctx);
    else {
      System.err.println("Iloc instruction not supported: " + ctx.getText());
      System.exit(-1);
    }

    return null;
  }

  @Override
  public Void visitPseudoOp(PseudoOpContext ctx) {
    if (ctx.STRING() != null) {
      String str = ctx.STRING_CONST().getText();
      allInstructions.add(new StringPseudoOp(ctx.LABEL().getText(), str.substring(1, str.length() - 1)));
    } else if (ctx.FLOAT() != null) {
      allInstructions.add(new FloatPseudoOp(ctx.LABEL().getText(), Float.parseFloat(ctx.FLOAT_CONST().getText())));
    } else if (ctx.GLOBAL() != null)
      allInstructions.add(new GlobalPseudoOp(ctx.LABEL().getText(), Integer.parseInt(ctx.NUMBER(0).getText()),
          Integer.parseInt(ctx.NUMBER(1).getText())));

    return null;
  }

  @Override
  public Void visitVirtualReg(VirtualRegContext ctx) {
    lastVR = new VirtualRegisterOperand(Integer.parseInt(ctx.VR().getText().substring(3)));
    return null;
  }

  @Override
  public Void visitImmediateVal(ImmediateValContext ctx) {
    if (ctx.LABEL() != null)
      lastOp = new LabelOperand(ctx.LABEL().getText());
    else
      lastOp = new ConstantOperand(Integer.parseInt(ctx.NUMBER().getText()));
    return null;
  }

  @Override
  public Void visitIlocInstruction(IlocInstructionContext ctx) {
    ctx.operation().accept(this);

    if (ctx.LABEL() != null)
      newI.setLabel(ctx.LABEL().getText());

    allInstructions.add(newI);

    return null;
  }

  private void readCode(CharStream code) throws IOException {

    // create a lexer that feeds off of input CharStream
    ilocLexer lexer = new ilocLexer(code);
    // create a buffer of tokens pulled from the lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    // create a parser that feeds off the tokens buffer
    ilocParser parser = new ilocParser(tokens);
    ParseTree t = null;
    try {
      t = parser.program();
    } catch (RuntimeException e) {
      System.err.println("Syntax error: " + e);
      System.exit(-1);
    }

    visit(t);
    buildRoutines();
  }

  protected abstract void optimizeCode();

  public void processCode(String ilocFileName) throws IOException {
    inputFileName = ilocFileName;
    readCode(CharStreams.fromFileName(ilocFileName));
    optimizeCode();

    if (prevPassAbbrev.equals(""))
      ilocFileName = ilocFileName.replace(".il", "." + passAbbrev + ".il");
    else
      ilocFileName = ilocFileName.replace("." + prevPassAbbrev + ".il", "." + passAbbrev + ".il");

    PrintWriter pw = new PrintWriter(ilocFileName);
    emitCode(pw);
    pw.close();
  }
}
