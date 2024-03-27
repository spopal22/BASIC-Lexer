class AssignmentNode extends StatementNode{
    private final Node var;
    private final Node assignedValue;

    AssignmentNode(Node var, Node assignedValue) {
        super();
        this.var = var;
        this.assignedValue = assignedValue;
    } //Constructor to initialize the node and other nodes

    public Node getVar(){
        return var;
    } //Method to get the variable

    public Node getAssignedValue(){
        return assignedValue;
    } //Method to get the assignedvalue

    @Override
    public String toString() {
        return "The assignment node is: " + var.toString() + "=" + assignedValue.toString();
    }
}