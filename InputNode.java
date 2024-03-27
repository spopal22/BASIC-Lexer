import java.util.LinkedList;

class InputNode extends StatementNode{
    private final String strings;
    private final LinkedList<String> variable; //Input is for either strings or variables so we make both

    InputNode(String strings, LinkedList<String> variable) {
        this.strings = strings;
        this.variable = variable;
    } //Constructor to initialize the string and list and whats within it

    public String getStrings(){
        return strings;
    } //Method to return the string

    public LinkedList<String> getVariable(){
        return variable;
    } //Method to return the variable

    @Override
    public String toString() {
        return "The input is: " + strings + ", and the variables are: " + String.join(", ", variable);
    }
}
