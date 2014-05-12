package tsa;

import java.util.*;
import java.io.*;
//import java.util.Scanner;
import soot.*;
import soot.util.*;
import soot.tagkit.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;

public class TSA2 {
	private Map<Unit, BoundedFlowSet> i_set;
	private Map<Unit, BoundedFlowSet> o_set;
	private Body body1;

	public TSA2(Body body2) {
		body1 = body2;
		i_set = new HashMap<Unit, BoundedFlowSet>();
		o_set = new HashMap<Unit, BoundedFlowSet>();
	}

	public List<Integer> get_fln() throws FileNotFoundException {
		BriefUnitGraph g1 = new BriefUnitGraph(body1);
		TSA1 a = new TSA1(body1);
		// First create the initial in, out and successor sets.
		for (Unit u1 : g1) {
			i_set.put(u1, a.crt_ISet(u1));
			o_set.put(u1, a.crt_OSet(u1));
		}

		boolean c = true;
		PseudoTopologicalOrderer<Unit> rev_DFS = new PseudoTopologicalOrderer<Unit>();
		List<Unit> ord_units = rev_DFS.newList(g1, true);
		while (c) {
			c = false;
			for (Unit u2 : ord_units) {
				// Note that these two are refs
				BoundedFlowSet o_set1 = o_set.get(u2);
				BoundedFlowSet i_set1 = i_set.get(u2);
				BoundedFlowSet old_i_set = (BoundedFlowSet) i_set1.clone();

				List<Unit> successor = g1.getSuccsOf(u2);
				List<BoundedFlowSet> successor_list = new ArrayList<BoundedFlowSet>(
						successor.size());
				for (Unit successor1 : successor)
					successor_list.add(o_set.get(successor1));
				a.mrg_OSets(successor_list, i_set1);
				if (!i_set1.equals(old_i_set))
					c = true;
				a.cal_OSet(u2, i_set1, o_set1);
			}
		}

		Chain<Unit> units = body1.getUnits();
		List<Integer> ln_nos = new ArrayList<Integer>();
		for (Unit u3 : units) {
			// We collect the line numbers of all those definitions that are
			// faint immediately after they're defined.
			for (ValueBox v1 : u3.getDefBoxes()) {
				if (i_set.get(u3).contains(v1.getValue())) {
					List<Tag> list = u3.getTags();
					Iterator<Tag> list1 = list.iterator();
					int t = 0;
					while (list1.hasNext()) {
						LineNumberTag tag1 = (LineNumberTag) list1.next();
						if (t == 0) {
							ln_nos.add(tag1.getLineNumber());
							t++;
						}
					}
				}
			}
		}
		return ln_nos;
	}
}

class Assign1 extends BodyTransformer {
	private static Assign1 instance = new Assign1();

	private Assign1() {
	}

	public static Assign1 v2() {
		return instance;
	}

	protected void internalTransform(Body body3, String phase_name,
			@SuppressWarnings("rawtypes") Map opts) {
		TSA2 a = new TSA2(body3);
		try {
			TSA_Main.add_fln(a.get_fln());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
