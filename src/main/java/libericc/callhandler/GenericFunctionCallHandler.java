package libericc.callhandler;

import heros.EdgeFunction;
import heros.FlowFunction;
import heros.edgefunc.EdgeIdentity;
import heros.flowfunc.Identity;
import libericc.edgefunc.GenericValueEdge;
import libericc.edgefunc.intent.IntentGetStringEdge;
import libericc.value.GeneralValue;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

import java.util.*;

public class GenericFunctionCallHandler extends CallHandler {
	@Override
	public Set<MethodSig> getTargets(){
		Set<MethodSig> targets = new HashSet<MethodSig>();
		targets.add(new MethodSig("android.content.Intent", "java.lang.String getStringExtra(java.lang.String)"));
		return targets;
	}
	
	@Override
	public FlowFunction<Value> getCallToReturnFlowFunction(Unit callSite, Unit returnSite, Value zeroValue) {
		if (callSite instanceof DefinitionStmt) {
			Stmt callStmt = (Stmt) callSite;
			final InstanceInvokeExpr iie = (InstanceInvokeExpr) callStmt.getInvokeExpr();
			final Value base = iie.getBase();
			final Value arg0 = iie.getArg(0);
			final Value lvalue = ((DefinitionStmt)callSite).getLeftOp();

			return new FlowFunction<Value>() {
				@Override
				public Set<Value> computeTargets(Value source) {
					if (recorded) {
						if (source.equivTo(zeroValue)) {
							Set<Value> ret = new HashSet<Value>();
							ret.add(zeroValue);
							ret.add(lvalue);
							return ret;
						}
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
			final List<Value> args = iie.getArgs();
			final Value lvalue = ((DefinitionStmt)callSite).getLeftOp();
			
			
			if (recorded) {
				if (callNode.equivTo(zeroValue) && returnSideNode.equivTo(lvalue)) {
					return new GenericValueEdge(((StringConstant) base).value, args);
				}
			}
		}
		return EdgeIdentity.v();
	}

}
