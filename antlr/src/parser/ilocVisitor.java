// Generated from iloc.g4 by ANTLR 4.7.2
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ilocParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ilocVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ilocParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ilocParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData(ilocParser.DataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#procedures}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedures(ilocParser.ProceduresContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure(ilocParser.ProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#ilocInstruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIlocInstruction(ilocParser.IlocInstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#frameInstruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameInstruction(ilocParser.FrameInstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#operationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationList(ilocParser.OperationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation(ilocParser.OperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#pseudoOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoOp(ilocParser.PseudoOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#virtualReg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtualReg(ilocParser.VirtualRegContext ctx);
	/**
	 * Visit a parse tree produced by {@link ilocParser#immediateVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmediateVal(ilocParser.ImmediateValContext ctx);
}