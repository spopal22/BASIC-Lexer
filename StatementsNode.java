import java.util.LinkedList;

public class StatementsNode {
    private final LinkedList<Node> statementsList; //LinkedList to store the list of statement nodes

    public StatementsNode(LinkedList<Node> statementsList) {
        this.statementsList = statementsList;
    } //Constructor to initialize the list and Node

    public LinkedList<Node> getStatementsList(){
        return statementsList;
    } //Method to retrieve the list of nodes

    @Override
    public String toString() {
        return "The StatementsNode is: " + String.join((CharSequence) ", ", (CharSequence) statementsList) + "";
    }
}
