import java.util.LinkedList;

class PrintNode extends StatementNode {
    private final LinkedList<Node> print; //LinkedList to store the list of nodes that we are going to print

    PrintNode(LinkedList<Node> print) {
        super();
        this.print = print;
    } //Constructor initializes the node and list of nodes

    public LinkedList<Node> getPrint(){
        return print;
    } //Method to retrieve the list of nodes we want to print

    @Override
    public String toString() {
        return "The Print node is: " + String.join((CharSequence) ", ", (CharSequence) print);
    }
}