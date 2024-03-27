public class Token {

    public enum TokenType {WORD, NUMBER, ENDOFLINE, PRINT, READ, INPUT, DATA, GOSUB, FOR, TO, STEP, NEXT, RETURN, IF, THEN, FUNCTION, WHILE, END, STRINGLITERAL, LESSEROR, GREATEROR, NOTEQUALS, EQUALS, LESSER, GREATER, LPAREN, RPAREN, PLUS, MINUS, MULTIPLY, DIVIDE, DOUBLEEQUALS, AND, INCREMENT, DECREMENT, COMMA, PERIOD, COLON, LABEL} //Use enum to define the different types of tokens

    private TokenType type;
    private int lineNum;
    private int pos;
    private String value; //Store values for type, linenum, pos, and value

    public Token(TokenType type, int lineNum, int pos, String value) {
        this.type = type;
        this.lineNum = lineNum;
        this.pos = pos;
        this.value = value;; //Initializes type, linenum, pos, and value
    }

    public TokenType getType() {
        return type;
    } //This is used for the unit test so we're able to get the type and verify its as expected

    public String getValue() {
        return value;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(Type: ").append(type).append(", Line number: ").append(lineNum)
                .append(", Character Position: ").append(pos); //Creates string builder to build string representation of token and appends all the relevant information
        if (value != null) {
            result.append(", Value: ").append(value); //As long as value isnt null appends value to token output as well
        }
        result.append(")");
        return result.toString(); //Closes off the parentheses and returns the result
    }
}
