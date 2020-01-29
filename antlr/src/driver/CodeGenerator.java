/**
 *
 */
package driver;

import java.io.FileNotFoundException;
import java.io.IOException;

import middleEnd.dbre.DominatorBasedRedundancyElimination;
import middleEnd.lvn.LocalValueNumbering;
import middleEnd.utils.OptimizationPass;

/**
 * @author carr
 *
 */
public class CodeGenerator {

	public static boolean emitCfg = false;

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
			} else if (args[i].equals("-gv")) {
				emitCfg = true;
				continue;
			} else {
				System.err.println("Invalid command option: " + args[i]);
				break;
			}
			pass.processCode(args[numargs - 1]);
		}
	}
}
