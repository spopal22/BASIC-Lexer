import java.util.LinkedList;

class DataNode extends StatementNode {
    private LinkedList<Node> data; //LinkedList to store the list of data statements we want to store

    public DataNode(LinkedList<Node> data) {
        this.data = data;
    } //Consturctor to initialize the list and nodes with it

    public LinkedList<Node> getData() {
        return data;
    } //Method to retrieve the list of data

    @Override
    public String toString() {
        return "The data node is: " + String.join((CharSequence) ", ", (CharSequence) data);
    }
}

