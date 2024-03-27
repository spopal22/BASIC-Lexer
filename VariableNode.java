public class VariableNode extends Node{
    private final String varName;

    public VariableNode(String varName) {
        this.varName = varName;
    } //Constructor to initilize the Node with the veriable name

    public String getVarName(){
        return varName;
    } //Method to get the name of the variable

    @Override
    public String toString(){
        return "The VariableNode is: " + varName;
    }
}
