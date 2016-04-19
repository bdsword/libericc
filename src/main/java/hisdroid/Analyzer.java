package hisdroid;

import java.util.Map;

import hisdroid.value.GeneralValue;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;

public interface Analyzer {
	public void analyze(SootMethod mainMethod);
	public Map<Value, GeneralValue> resultsAt(Unit stmt);
	public TriLogic branchAt(IfStmt stmt);
}

