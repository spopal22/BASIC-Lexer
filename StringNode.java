class StringNode extends Node {
    private String value; //LinkedList to store the list of nodes that we are going to print

    public StringNode(String value) {
        this.value = value;
    } //Constructor initializes the node and list of nodes

    public String getValue() {
        return value;
    }//Method to retrieve the list of nodes we want to print

    @Override
    public String toString() {
        return "The String node is: " + String.join((CharSequence) ", ", (CharSequence) value);
    }
}