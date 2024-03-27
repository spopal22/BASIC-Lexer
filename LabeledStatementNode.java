class LabeledStatementNode extends StatementNode {
    private final String label;
    private final Node statement;

    public LabeledStatementNode(String label, Node statement) {
        this.label = label;
        this.statement = statement;
    }

    public String getLabel() {
        return label;
    }

    public Node getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return "The label is: " + label + ", and the statement is" + statement;
    }
}