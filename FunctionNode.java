import java.util.List;

public class FunctionNode extends Node {
    private final String functionName;
    private final List<Node> parameters;

    public FunctionNode(String functionName, List<Node> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Node> getParameters() {
        return parameters;
    }
}