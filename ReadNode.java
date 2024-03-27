import java.util.LinkedList;

class ReadNode extends StatementNode{
    private LinkedList<String> variables; //LinkedList to store the different variables in a list

    public ReadNode(LinkedList<String> variables){
        this.variables = variables;
    } //Constructor initializes the list and the nodes within it

    public LinkedList<String> getRead(){
        return variables;
    } //Method to retrieve the list of variables

    @Override
    public String toString() {
        return "The read node is: " + String.join((CharSequence) ", ", (CharSequence) variables);
    }
}
