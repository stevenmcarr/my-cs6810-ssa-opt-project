/**
 *
 */
package driver;

import java.io.FileNotFoundException;
import java.io.IOException;

import middleEnd.dbre.DominatorBasedRedundancyElimination;
import middleEnd.dce.DeadCodeElimination;
import middleEnd.lvn.LocalValueNumbering;
import middleEnd.utils.OptimizationPass;

/**
 * @author carr
 *
 */
public class CodeGenerator {

	public static boolean emitCfg = false;
	public static boolean emitDT = false;
	public static boolean emitPDT = false;
	public static boolean emitDF = false;
	public static boolean emitPDF = false;
	public static boolean emitLVA = false;
	public static boolean emitSSA = false;

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException, IOException {
		int numargs = args.length;
		OptimizationPass pass = null;
		String prevPass = "";

		for (int i = 0; i < numargs - 1; i++) {
			if (args[i].equals("-lvn")) {
				pass = new LocalValueNumbering(prevPass, "lvn");
				prevPass = "lvn";
			} else if (args[i].equals("-dbre")) {
				pass = new DominatorBasedRedundancyElimination(prevPass, "dbre");
				prevPass = "dbre";
			} else if (args[i].equals("-dce")) {
				pass = new DeadCodeElimination(prevPass, "dce");
				prevPass = "dce";
			} else if (args[i].equals("-cfg")) {
				emitCfg = true;
				continue;
			} else if (args[i].equals("-dt")) {
				emitDT = true;
				continue;
			} else if (args[i].equals("-pdt")) {
				emitPDT = true;
				continue;
			} else if (args[i].equals("-df")) {
				emitDF = true;
				continue;
			} else if (args[i].equals("-pdf")) {
				emitPDF = true;
				continue;
			} else if (args[i].equals("-lva")) {
				emitLVA = true;
				continue;
			} else if (args[i].equals("-ssa")) {
				emitSSA = true;
				continue;
			} else {
				System.err.println("Invalid command option: " + args[i]);
				break;
			}
			if (pass != null) {
				pass.processCode(args[numargs - 1]);
				pass = null;
			}
		}
	}
}
