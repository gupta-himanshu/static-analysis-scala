package tsa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
 
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.util.Chain;
 
public class MethodCallCollector {
 
  static String directoryToAnalyze;
  
  static List<String> lSootClasspath = new ArrayList<String>();
  static void init() {
    lSootClasspath.add(directoryToAnalyze);
    StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), classpathDelimiter);
    while (st.hasMoreTokens()) {lSootClasspath.add(st.nextToken());}
  }
  
  /** The file in which the traces are put */
  static String datasetFileName = "tsa.data.java";
 
  
  public static void main(String[] args) throws Exception{
    
    if (args.length == 1) {
      directoryToAnalyze = args[0];
    }
 
    
    for(String file : lSootClasspath) {
      if (!new File(file).exists()) {
        throw new IllegalArgumentException(file + " does not exist");
      }
    }
    
    System.err.println("Analyzing "+directoryToAnalyze);
    MethodCallCollector analyzer = new MethodCallCollector();
    analyzer.analyze(directoryToAnalyze);
  }
  
  public void analyze(String toBeAnalyzed) throws Exception{
    
    appOut = new BufferedWriter(new FileWriter(datasetFileName));
    PackManager.v().getPack("jtp").add(
        new Transform("jtp.myTransform", new BodyTransformer() {
 
          protected void internalTransform(Body body, String phase, @SuppressWarnings("rawtypes") Map options) {
            
            
            Chain<Local> locals = body.getLocals();
 
            HashMap<String, Variable> variables  = new HashMap<String, Variable>();
            
            // first getting Locals
            for(Local l:locals) { // for each variable of the method
              Variable aVariable  = new Variable();
              aVariable.name=l.getName().toString();
              aVariable.type = l.getType().toString();
              variables.put(l.getName(),aVariable);
            }
            
            PatchingChain<Unit> units = body.getUnits();
            for (Unit u: units) { // for each statement
              Stmt s = (Stmt)u;
              if (s.containsInvokeExpr()) {
                InvokeExpr invokeExpr = s.getInvokeExpr();
                if (invokeExpr instanceof InstanceInvokeExpr) {
                  InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) invokeExpr;
                  Value v = instanceInvokeExpr.getBase();
                  Variable aVariable = variables.get(((Local)v).getName());
                  aVariable.methodCalls.add(instanceInvokeExpr.getMethod().getName());
                }
              }
            }
              
            // simple resolving            
            for (Unit u: units) { // for each statement
              Stmt s = (Stmt)u;
              if (s instanceof AssignStmt) {
                AssignStmt ass = (AssignStmt)s;
                Value left = ass.getLeftOp();
                Value right = ass.getRightOp();
                if (left instanceof Local && right instanceof Local) {
                  Variable vleft = variables.get(((Local)left).getName());
                  Variable vright = variables.get(((Local)right).getName());
                  for (String mc: vright.methodCalls) {
                    vleft.methodCalls.add(mc);
                  }
                }
              }
            }
            
            // output the variables
            for (Variable aVariable: variables.values()) {
              if ((aVariable.methodCalls.size()>1) ) {
                System.out.println((nbTraces++)+" "+aVariable);
                try {
                  appOut.write(aVariable+"\n");
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            }
 
        
        } // end internalTransform
          
        } /* end new Transform */));
    
 
    String sootClasspath = ".";
    for (String jar : lSootClasspath) { sootClasspath+=classpathDelimiter+jar;}    
    
    String[]  myArgs = 
    {
        "-cp", sootClasspath,
        "-process-dir", toBeAnalyzed,
        "-include","org.apache." /* by default, org.apache. is discarded by soot */
    };
    
    Options.v().parse( myArgs);
    Options.v().set_keep_line_number(true);
    Options.v().set_output_format(Options.output_format_none);
    
    System.err.println(Arrays.toString(myArgs));
    
 
    Scene.v().loadNecessaryClasses();
 
    PackManager.v().runPacks();
    PackManager.v().writeOutput();
 
    appOut.close();
  }
 
  /** The number of collected traces */
  int nbTraces;
  
  static String classpathDelimiter ="/";
  
  Writer appOut ;
 
}
 
 
/** used to store traces */
class Variable {
  String name = "!";
  String type = "!";
  Local l;
 
  List<String> methodCalls = new ArrayList<String>();
 
  public String toString() {
    String callString = "";
    for (Iterator<String> iterator = methodCalls.iterator(); iterator.hasNext();) {
      String call = iterator.next();
      callString += call;
      if(iterator.hasNext()) {
        callString += " ";
      }
    }
    
    return name + " " + type + " " + callString;
  }
  
}
