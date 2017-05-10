package libericc.edgefunc;


import heros.EdgeFunction;
import libericc.value.BottomValue;
import libericc.value.GeneralValue;
import libericc.value.IntentValue;
import libericc.value.PrimitiveDataValue;
import org.json.JSONException;
import org.json.JSONObject;
import soot.Value;

import java.util.LinkedList;
import java.util.List;

public class GenericValueEdge extends EdgeFunctionTemplate implements IJoinableEdge {
	List<IJoinableEdge> edgeList = new LinkedList<>();
	String methodName;
	List<Value> args;

	public GenericValueEdge(){
	}

	public GenericValueEdge(String methodName, List<Value> args) {
	    this.methodName = methodName;
	    this.args = args;
	}

	public GenericValueEdge(GenericValueEdge old, EdgeFunction<GeneralValue> next){
		this.next = next;
	}

	@Override
	public EdgeFunctionTemplate copy() {
		return new GenericValueEdge(this, next);
	}

	@Override
	public GeneralValue computeTargetImplementation(GeneralValue source) {
		GeneralValue joinResult = null;
	    for (IJoinableEdge joinableEdge : edgeList) {
	        if (joinResult == null) {
	            joinResult = joinableEdge.computeTargetImplementation(source);
            } else {
                joinResult.joinWith(joinableEdge.computeTargetImplementation(source));
            }
		}
		return joinResult;
	}

	@Override
	public EdgeFunction<GeneralValue> joinWithFirstEdge(EdgeFunction<GeneralValue> otherFunction) {
		if (otherFunction instanceof GenericValueEdge) {
			GenericValueEdge otherEdge = (GenericValueEdge)otherFunction;
			GenericValueEdge joinResult = new GenericValueEdge();
			joinResult.edgeList.add(this);
			joinResult.edgeList.add(otherEdge);
			return joinResult;
		}
		return new AllBottom<GeneralValue>(BottomValue.v());
	}

	@Override
	protected boolean equalToFirstEdge(EdgeFunction<GeneralValue> other) {
		if (other instanceof GenericValueEdge) {
			GenericValueEdge otherEdge = (GenericValueEdge) other;

			if (args.size() != otherEdge.args.size()) {
			    return false;
            }

            if (!methodName.equals(otherEdge.methodName)) {
			    return false;
            }

            for (int i = 0; i < args.size(); i += 1) {
			    if (!args.get(i).equals(otherEdge.args.get(i))) {
			        return false;
                }
            }

			return true;
		}
		return false;
	}

	@Override
	public String edgeToString() {
	    StringBuilder argsStr = new StringBuilder();
	    if (args.size() != 0) {
	        argsStr.append(args.get(0).toString());
        }
	    for (int i = 1; i < args.size(); i += 1) {
	        argsStr.append(args.get(i).toString());
        }
		return String.format("%s(\"%s\")", methodName, argsStr);
	}
}
