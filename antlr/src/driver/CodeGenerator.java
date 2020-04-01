/**
 *
 */
package driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import middleEnd.utils.OptimizationPass;

/**
 * @author carr
 *
 */
public class CodeGenerator {
	private static LinkedList<String> passFlags = new LinkedList<String>();
	private static LinkedList<String> passClassName = new LinkedList<String>();

	private static void registerOptimizationPass(String flag, String className) {
		passFlags.add(flag);
		passClassName.add(className);
	}

	private static void generateOptimizationPasses() {
		CodeGenerator.registerOptimizationPass("-lvn", "middleEnd.lvn.LocalValueNumbering");
		CodeGenerator.registerOptimizationPass("-dbre", "middleEnd.dbre.DominatorBasedRedundancyElimination");
		CodeGenerator.registerOptimizationPass("-dce", "middleEnd.dce.DeadCodeElimination");
		CodeGenerator.registerOptimizationPass("-ruc", "middleEnd.dce.RemoveUnreachableCode");
		CodeGenerator.registerOptimizationPass("-ra",  "backend.ra.ChaitinBriggs");
		CodeGenerator.registerOptimizationPass("-cse", "middleEnd.gcse.GCSE");
	}

	private static String getPassClassName(String flag) {
		int index = passFlags.indexOf(flag);
		if (index >= 0)
			return passClassName.get(index);
		else
			return null;
	}

	public static boolean emitCfg = false;
	public static boolean emitDT = false;
	public static boolean emitPDT = false;
	public static boolean emitDF = false;
	public static boolean emitPDF = false;
	public static boolean emitLVA = false;
	public static boolean emitSSA = false;
	public static boolean emitIG = false;
	public static boolean emitDUCode = false;

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws FileNotFoundException, IOException {
		generateOptimizationPasses();
		int numargs = args.length;
		OptimizationPass pass = null;
		String prevPass = "";

		for (int i = 0; i < numargs - 1; i++) {
			String passName = getPassClassName(args[i]);
			if (passName != null) {
				Class<OptimizationPass> cl = null;
				String passString = null;
				try {
					cl = (Class<OptimizationPass>) Class.forName(passName);
					Constructor<OptimizationPass> con = cl.getConstructor(String.class, String.class);
					passString = args[i].substring(1, args[i].length());
					pass = (OptimizationPass) con.newInstance(prevPass, passString);
				} catch (ClassNotFoundException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				} catch (NoSuchMethodException | SecurityException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				} catch (InstantiationException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				} catch (IllegalAccessException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				} catch (IllegalArgumentException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				} catch (InvocationTargetException e) {
					System.err.println("Option " + args[i] + " not supported.");
					continue;
				}
				prevPass = passString;
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
			} else if (args[i].equals("-ig")) {
				emitIG = true;
				continue;
			} else if (args[i].equals("-du")) {
				emitDUCode = true;
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
