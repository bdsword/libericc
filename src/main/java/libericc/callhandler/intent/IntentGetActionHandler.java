package libericc.callhandler.intent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import heros.EdgeFunction;
import heros.FlowFunction;
import heros.edgefunc.EdgeIdentity;
import heros.flowfunc.Identity;
import libericc.callhandler.CallHandler;
import libericc.edgefunc.intent.IntentGetActionEdge;
import libericc.value.GeneralValue;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;

public class IntentGetActionHandler extends CallHandler {
	@Override
	public Set<MethodSig> getTargets(){
		Set<MethodSig> targets = new HashSet<MethodSig>();
		targets.add(new MethodSig("android.content.Intent", "java.lang.String getAction()"));
		return targets;
	}
	
	@Override
	public FlowFunction<Value> getCallToReturnFlowFunction(Unit callSite, Unit returnSite, Value zeroValue) {
		if (callSite instanceof DefinitionStmt) {
			Stmt callStmt = (Stmt) callSite;
			final InstanceInvokeExpr iie = (InstanceInvokeExpr) callStmt.getInvokeExpr();
			final Value base = iie.getBase();
			final Value lvalue = ((DefinitionStmt)callSite).getLeftOp();
			
			return new FlowFunction<Value>() {
				@Override
				public Set<Value> computeTargets(Value source) {
					if (source.equivTo(base)) {
						Set<Value> ret = new HashSet<Value>();
						ret.add(source);
						ret.add(lvalue);
						return ret;
					}
					if (source.equivTo(lvalue)) {
						return Collections.emptySet();
					}
					return Collections.singleton(source);
				}
			};
		}
		return Identity.v();
	}

	@Override
	public EdgeFunction<GeneralValue> getCallToReturnEdgeFunction(Unit callSite, Value callNode, Unit returnSite,
			Value returnSideNode, Value zeroValue) {
		if (callSite instanceof DefinitionStmt) {
			Stmt callStmt = (Stmt) callSite;
			final InstanceInvokeExpr iie = (InstanceInvokeExpr) callStmt.getInvokeExpr();
			final Value base = iie.getBase();
			final Value lvalue = ((DefinitionStmt)callSite).getLeftOp();
			
			if (callNode.equivTo(base) && returnSideNode.equivTo(lvalue)) {
				return new IntentGetActionEdge();
			}
		}
		return EdgeIdentity.v();
	}

}
