public class WHILENode extends StatementNode {
    private final Node booleanExpression;
    private final String end;

    public WHILENode(Node booleanExpression, String end) {
        this.booleanExpression = booleanExpression;
        this.end = end;
    }

    public Node getBoolean() {
        return booleanExpression;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "WHILE " + booleanExpression.toString() + " END " + end;
    }
}