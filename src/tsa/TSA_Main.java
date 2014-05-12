package tsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class TSA_Main {
	private static List<Integer> faint_ln_nos = new ArrayList<Integer>();

	public static void add_fln(List<Integer> ln_nos) {
		faint_ln_nos.addAll(ln_nos);
	}

	public static void main(String[] args) {
		List<String> soot_args = new ArrayList<String>(Arrays.asList(args));
		soot_args.add("tsa.Scala_tc1");
		System.out.println("Started Execution");
		soot_args.add(0, "-keep-line-number");
		soot_args.add("-output-format");
		soot_args.add("none");
		Options.v()
				.set_soot_classpath(
						"/home/himanshu/static-analysis-scala/bin:/home/himanshu/jdk1.7.0_60/jre/lib/rt.jar:/home/himanshu/Scala/scala-2.10.3/lib/scala-library.jar");
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.faintvariableanalysis", Assign1.v2()));

		soot.Main.main(soot_args.toArray(args));

		/**
		 * Print all the line numbers
		 */
		Collections.sort(faint_ln_nos);
		System.out.println("Line Numbers for other warnings are :");
		for (int ln_no : faint_ln_nos) {
			System.out.println(ln_no);
		}
	}
}