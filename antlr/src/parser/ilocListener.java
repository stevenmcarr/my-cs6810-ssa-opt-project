// Generated from iloc.g4 by ANTLR 4.7.2
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ilocParser}.
 */
public interface ilocListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ilocParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ilocParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ilocParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#data}.
	 * @param ctx the parse tree
	 */
	void enterData(ilocParser.DataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#data}.
	 * @param ctx the parse tree
	 */
	void exitData(ilocParser.DataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#procedures}.
	 * @param ctx the parse tree
	 */
	void enterProcedures(ilocParser.ProceduresContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#procedures}.
	 * @param ctx the parse tree
	 */
	void exitProcedures(ilocParser.ProceduresContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#procedure}.
	 * @param ctx the parse tree
	 */
	void enterProcedure(ilocParser.ProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#procedure}.
	 * @param ctx the parse tree
	 */
	void exitProcedure(ilocParser.ProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#ilocInstruction}.
	 * @param ctx the parse tree
	 */
	void enterIlocInstruction(ilocParser.IlocInstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#ilocInstruction}.
	 * @param ctx the parse tree
	 */
	void exitIlocInstruction(ilocParser.IlocInstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#frameInstruction}.
	 * @param ctx the parse tree
	 */
	void enterFrameInstruction(ilocParser.FrameInstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#frameInstruction}.
	 * @param ctx the parse tree
	 */
	void exitFrameInstruction(ilocParser.FrameInstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#operationList}.
	 * @param ctx the parse tree
	 */
	void enterOperationList(ilocParser.OperationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#operationList}.
	 * @param ctx the parse tree
	 */
	void exitOperationList(ilocParser.OperationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation(ilocParser.OperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation(ilocParser.OperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#pseudoOp}.
	 * @param ctx the parse tree
	 */
	void enterPseudoOp(ilocParser.PseudoOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#pseudoOp}.
	 * @param ctx the parse tree
	 */
	void exitPseudoOp(ilocParser.PseudoOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#virtualReg}.
	 * @param ctx the parse tree
	 */
	void enterVirtualReg(ilocParser.VirtualRegContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#virtualReg}.
	 * @param ctx the parse tree
	 */
	void exitVirtualReg(ilocParser.VirtualRegContext ctx);
	/**
	 * Enter a parse tree produced by {@link ilocParser#immediateVal}.
	 * @param ctx the parse tree
	 */
	void enterImmediateVal(ilocParser.ImmediateValContext ctx);
	/**
	 * Exit a parse tree produced by {@link ilocParser#immediateVal}.
	 * @param ctx the parse tree
	 */
	void exitImmediateVal(ilocParser.ImmediateValContext ctx);
}