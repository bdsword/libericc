package libericc.edgefunc;


import libericc.value.GeneralValue;

public interface IJoinableEdge {
    GeneralValue computeTargetImplementation(GeneralValue source);
}
