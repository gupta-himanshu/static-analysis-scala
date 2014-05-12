package tsa;

import java.util.*;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import soot.util.*;

public class TSA1 {
	static int state = -1;
	private FlowUniverse<Local> Vars;
	private BoundedFlowSet Univ_set;
	private Map<Unit, BoundedFlowSet> use_set;
	private Map<Unit, BoundedFlowSet> def_set;

	public TSA1(Body body) {
		Chain<Local> ls = body.getLocals();
		Vars = new CollectionFlowUniverse<Local>(ls);
		Univ_set = new ArrayPackedSet(Vars);
		for (Local ls1 : ls)
			Univ_set.add(ls1);
		HashMap<String, Variable> variables = new HashMap<String, Variable>();

		// first getting Locals
		for (Local l : ls) { // for each variable of the method
			Variable aVariable = new Variable();
			aVariable.name = l.getName().toString();
			aVariable.type = l.getType().toString();
			variables.put(l.getName(), aVariable);
		}

		PatchingChain<Unit> units = body.getUnits();
		int count = 0;
		for (Unit u : units) { // for each statement
			Stmt s = (Stmt) u;
			if (s.containsInvokeExpr()) {
				InvokeExpr invokeExpr = s.getInvokeExpr();
				if (invokeExpr instanceof InstanceInvokeExpr) {
					InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) invokeExpr;
					Value v = instanceInvokeExpr.getBase();
					Variable aVariable = variables.get(((Local) v).getName());
					int i = 0;
					if (i == instanceInvokeExpr.getMethod().getName()
							.compareTo("iterator")) {
						state = 0;
					}
					if (i == instanceInvokeExpr.getMethod().getName()
							.compareTo("next")) {
						count++;
						if (state == -1)
							state = -1;
						if (state == 0 && count >= 3)
							state = 2;
						if (state == 1)
							state = 0;
						if (state == 2)
							state = 2;
					}
					if (i == instanceInvokeExpr.getMethod().getName()
							.compareTo("hasNext")) {
						if (state == -1)
							state = 1;
						if (state == 0 || state == 2 || state == 1)
							state = 1;
					}
					if (i == instanceInvokeExpr.getMethod().getName()
							.compareTo("intValue")
							|| i == instanceInvokeExpr.getMethod().getName()
									.compareTo("println")
							|| i == instanceInvokeExpr.getMethod().getName()
									.compareTo("print")) {
						if (state == 2)
							state = -1;
					}
					if (instanceInvokeExpr.getMethod().toString()
							.contains("(java.util.Iterator")) {
						System.out
								.print("Do not pass iterator as argument in line no. = ");
						List<Tag> list = u.getTags();
						Iterator<Tag> list1 = list.iterator();
						while (list1.hasNext()) {
							LineNumberTag tag1 = (LineNumberTag) list1.next();
							System.out.println(tag1);
						}
					}
					if (state == 2) {
						System.out
								.print("Improper Iterator Usage in Line no. = ");
						List<Tag> list = u.getTags();
						Iterator<Tag> list1 = list.iterator();
						while (list1.hasNext()) {
							LineNumberTag tag1 = (LineNumberTag) list1.next();
							System.out.println(tag1);
						}
					}
					aVariable.methodCalls.add(instanceInvokeExpr.getMethod()
							.getName());
				}
			}
		}

		// simple resolving
		for (Unit u : units) { // for each statement
			Stmt s = (Stmt) u;
			if (s instanceof AssignStmt) {
				AssignStmt ass = (AssignStmt) s;
				Value left = ass.getLeftOp();
				Value right = ass.getRightOp();
				if (left instanceof Local && right instanceof Local) {
					Variable vleft = variables.get(((Local) left).getName());
					Variable vright = variables.get(((Local) right).getName());
					for (String mc : vright.methodCalls) {
						vleft.methodCalls.add(mc);
					}
				}
			}
		}

		// output the variables
		for (Variable aVariable : variables.values()) {
			if ((aVariable.methodCalls.size() > 1)) {
			}
		}

		use_set = new HashMap<Unit, BoundedFlowSet>();
		def_set = new HashMap<Unit, BoundedFlowSet>();

		// Creating graph
		UnitGraph graph = new BriefUnitGraph(body);
		for (Unit u1 : graph) {
			BoundedFlowSet def_set1 = new ArrayPackedSet(Vars);
			BoundedFlowSet use_set1 = new ArrayPackedSet(Vars);
			for (ValueBox v : u1.getDefBoxes()) {
				if (v.getValue() instanceof Local) {
					def_set1.add(v.getValue());
				}
			}
			for (ValueBox v : u1.getUseBoxes()) {
				if (v.getValue() instanceof Local) {
					use_set1.add(v.getValue());
				}
			}

			def_set.put(u1, def_set1);
			use_set.put(u1, use_set1);
		}
	}

	public BoundedFlowSet crt_ISet(Unit u2) {
		// Initial value for in set is the universal set
		return (BoundedFlowSet) Univ_set.clone();
	}

	public BoundedFlowSet crt_OSet(Unit u3) {
		// Initial value for the out set is the universal set
		return (BoundedFlowSet) Univ_set.clone();
	}

	public void mrg_OSets(List<BoundedFlowSet> O_sets, BoundedFlowSet I_set1) {
		// Take the intersection of the out sets with the in set.
		for (BoundedFlowSet set : O_sets)
			I_set1.intersection(set);
	}

	public void cal_OSet(Unit u4, BoundedFlowSet I_set2, BoundedFlowSet O_set) {
		BoundedFlowSet def_set1 = def_set.get(u4);
		BoundedFlowSet iCompl = (BoundedFlowSet) I_set2.clone();
		iCompl.complement();
		iCompl.intersection(def_set1);
		if (iCompl.size() == 0) {
			I_set2.copy(O_set);
			O_set.difference(use_set.get(u4));
		} else {
			I_set2.union(def_set1, O_set);
			O_set.difference(use_set.get(u4));
		}
	}
}
