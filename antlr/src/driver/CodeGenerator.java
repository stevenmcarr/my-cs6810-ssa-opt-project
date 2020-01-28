/**
 *
 */
package driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import middleEnd.IlocGenerator;
import middleEnd.opt.LocalValueNumbering;
import parser.ilocLexer;
import parser.ilocParser;

/**
 * @author carr
 *
 */
public class CodeGenerator {

	public static void processCode(CharStream code, String fn) throws IOException {

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

		IlocGenerator ig = new IlocGenerator();
		ig.visit(t);
		ig.buildRoutines();
		ig.performTypeAssignment();

		LocalValueNumbering lvn = new LocalValueNumbering(ig);
		lvn.performValueNumbering();

		PrintWriter pw = new PrintWriter(fn.replace(".il", ".lvn.il"));
		ig.emitCode(pw);
		pw.close();
	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException, IOException {
		processCode(CharStreams.fromFileName(args[0]), args[0]);
	}
}
