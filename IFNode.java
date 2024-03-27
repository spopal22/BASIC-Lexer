public class IFNode extends StatementNode {
    private final Node condition;
    private final String end;

    public IFNode(Node condition, String end) {
        this.condition = condition;
        this.end = end;
    }

    public Node getCondition() {
        return condition;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "If, " + condition + " then " + end;
    }
}