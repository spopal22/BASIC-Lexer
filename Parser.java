import java.io.IOException;
import java.util.LinkedList;

public class Parser {
    private TokenManager tokenManager;
    private CodeHandler codeHandler;

    public Parser(LinkedList<Token> tokenList) {
        this.tokenManager = new TokenManager(tokenList); //Initialize parser with list of tokens
        this.codeHandler = codeHandler;
    }

    public boolean AcceptSeparators() {
        boolean separator = false;
        while (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.ENDOFLINE) { //Removes endofline token
            tokenManager.matchAndRemove(Token.TokenType.ENDOFLINE);
            separator = true;
        }
        return separator;
    } //If seperator is accepted returns true otherwise leaves it false

    public Node parse() throws IOException {
        LinkedList<StatementsNode> statements = new LinkedList<>();

        while (tokenManager.MoreTokens()) {
            StatementsNode statement = Statements();
            if (statement != null) {
                statements.add(statement);
            }
            AcceptSeparators();
        }

        return new ProgramNode(statements);
    } //Adjusted parse to call statements instead of expressions

    private Node Statement() {
        Lexer lexer = new Lexer(codeHandler);
        Token currentToken = lexer.getCurrentToken();

        // Check if the current token is a label
        if (currentToken != null && currentToken.getType() == Token.TokenType.LABEL) {
            String label = currentToken.getValue();
            lexer.swallow(); // Move to the next token after the label

            // Check if the next token is a colon
            if (lexer.getType() != null && lexer.getType() == Token.TokenType.COLON) {
                lexer.swallow(); // Move past the colon

                // Recursively parse the statement after the label
                Node statement = Statement();

                // Create a LabeledStatementNode with the label and the parsed statement
                LabeledStatementNode labeledStatementNode = new LabeledStatementNode(label, statement);
                return labeledStatementNode;
            }
        }

        Node statementNode = PrintStatement();
        if (statementNode != null) {
            return statementNode;
        }

        statementNode = assignment();
        if (statementNode != null) {
            return statementNode;
        }

        return null;
    }

    private Node parseBooleanExpression() throws IOException {
        Node left = parse(); //Parses left
        Token operatorToken = tokenManager.Peek(0).orElse(null); //Peeks at next token to check for operator

        if (operatorToken != null && (operatorToken.getType() == Token.TokenType.GREATER ||
                operatorToken.getType() == Token.TokenType.GREATEROR ||
                operatorToken.getType() == Token.TokenType.LESSER ||
                operatorToken.getType() == Token.TokenType.LESSEROR ||
                operatorToken.getType() == Token.TokenType.NOTEQUALS ||
                operatorToken.getType() == Token.TokenType.EQUALS)) { //Checks if next token is a comparison operator

            tokenManager.matchAndRemove(operatorToken.getType());
            Node right = parse(); //Swallows that token and then parses the right
            return new BooleanNode(left, operatorToken.getType(), right); //Creates a booleannode that reprsenets the boolean expression
        }

        return left;
    }

    private Node parseFunctionInvocation() throws IOException {
        Token functionToken = tokenManager.matchAndRemove(Token.TokenType.FUNCTION)
                .orElse(null); //Match and removes function token

        if (functionToken != null) { //Checks if a function token gets found
            if (!tokenManager.matchAndRemove(Token.TokenType.LPAREN).isPresent()) {
                throw new RuntimeException("Left parenthesis expected");
            } //makes sure left parenthsis folows the function name

            LinkedList<Node> parameters = parseParameterList(); //parses full list of parameters

            if (!tokenManager.matchAndRemove(Token.TokenType.RPAREN).isPresent()) {
                throw new RuntimeException("Right parenthesis expected");
            } //Makes sure right parenthesis after paremeter

            return new FunctionNode(functionToken.getValue(), parameters); //Creates the new function node following the rules
        }

        return null;
    }

    private LinkedList<Node> parseParameterList() throws IOException {
        LinkedList<Node> parameters = new LinkedList<>();

        while (true) { //Loops until right parenthesis is found
            if (tokenManager.Peek(0).orElse(null).getType() == Token.TokenType.RPAREN) {
                break; //Checks if next token is right parenthesis which would mean the parameters are over
            }

            Node parameter = parse(); //parses each parameter

            if (parameter != null) { //If it gets parses successfuly it gets added to list of parameters
                parameters.add(parameter);
            } else {
                throw new RuntimeException("Invalid parameter");
            }

            if (tokenManager.Peek(0).orElse(null).getType() == Token.TokenType.COMMA) {
                tokenManager.matchAndRemove(Token.TokenType.COMMA);
            } else if (tokenManager.Peek(0).orElse(null).getType() != Token.TokenType.RPAREN) {
                throw new RuntimeException("Expected a comma in between parameters");
            } //Checks if paramters are seperated by a comma otherwise it whats for right parenthesis
        }

        return parameters; //Returns list of parameters
    }

    private StatementsNode Statements() throws IOException {
        LinkedList<Node> statements = new LinkedList<>();

        while (tokenManager.MoreTokens()) { //parses until no more tokens are present
            Node statement = Statement();
            if (statement != null) {
                statements.add(statement);
            } else {
                break; //parses every statement and adds it to the list
            }
        }

        return new StatementsNode(statements); //Returns the statement node containing all the statements
    }

    private Node PrintStatement() {
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() != Token.TokenType.PRINT) {
            return null; //Checks if next token is a PRINT token and if it isnt it returns null
        }

        tokenManager.matchAndRemove(Token.TokenType.PRINT); //Use matchAndRemove on the print token
        LinkedList<Node> printList = new LinkedList<>();

        Node expression = expression();
        while (expression != null) { //Parses through the whole list of items that were printing
            printList.add(expression);

            if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.COMMA) {
                tokenManager.matchAndRemove(Token.TokenType.COMMA);
                expression = expression(); //Checks ahead for any commas to seperate the different items we're meant to print, matchAndRemoves the comma and then parses the next expression
            } else {
                break; //Breaks loop if there are no more items to print
            }
        }

        return new PrintNode(printList);//Returns new node with all the items we're meant to print
    }

    public LinkedList<Node> printList() {
        LinkedList<Node> expressions = new LinkedList<>(); //Initialize list for all the expressions

        Node expression = expression(); //Parse the expression
        if (expression != null) {
            expressions.add(expression); //When expression gets parsed it gets added to the list
        } else {
            return null; //if nothing is parsed it returns null
        }

        while (tokenManager.matchAndRemove(Token.TokenType.COMMA).isPresent()) {
            expression = expression();
            if (expression != null) { //PrintList continues parsing expressions that are seperated by commas and adds to the list
                expressions.add(expression);
            } else {
                break; //If nothing is parsed breaks the loop
            }
        }

        return expressions; //Returns the expressions
    }

    private Node assignment() {
        // Expect a variable name
        Token variableToken = tokenManager.matchAndRemove(Token.TokenType.WORD)
                .orElse(null);
        if (variableToken == null) {
            return null; // No variable name found
        }

        // Expect an equals sign
        if (!tokenManager.matchAndRemove(Token.TokenType.EQUALS).isPresent()) {
            return null; // Equals sign not found
        }

        // Expect an expression
        Node expression = expression();
        if (expression == null) {
            return null; // No expression found
        }

        // Create and return the AssignmentNode
        return new AssignmentNode(new VariableNode(variableToken.getValue()), expression);
    }

    Node expression() {
        Node left = term();

        while (tokenManager.Peek(0).isPresent() &&
                (tokenManager.Peek(0).get().getType() == Token.TokenType.PLUS ||
                        tokenManager.Peek(0).get().getType() == Token.TokenType.MINUS)) { //Parses while + or - is present
            Token operator = tokenManager.matchAndRemove(Token.TokenType.PLUS)
                    .orElse(tokenManager.matchAndRemove(Token.TokenType.MINUS).orElse(null)); //Checks for matches and removes token
            Node right = term(); //Parses the right
            left = new MathOpNode(left, operator.getType(), right); //Creates mathOpNode and updates left node
        }

        return left; //Returns left node
    }

    private Node term() {
        Node left = factor();

        while (tokenManager.Peek(0).isPresent() && //Parses while * or / are in stream
                (tokenManager.Peek(0).get().getType() == Token.TokenType.MULTIPLY ||
                        tokenManager.Peek(0).get().getType() == Token.TokenType.DIVIDE)) {
            Token operator = tokenManager.matchAndRemove(Token.TokenType.MULTIPLY)
                    .orElse(tokenManager.matchAndRemove(Token.TokenType.DIVIDE).orElse(null)); //Checks for matches and removes token
            Node right = factor();
            left = new MathOpNode(left, operator.getType(), right); //Creates mathopnode and updates left node
        }

        return left; //Return left node
    }

    private Node factor() {
        Token nextToken = tokenManager.Peek(0).orElse(null); //Peeks at next token
        if (nextToken == null) {
            return null; //If no tokens, return null
        }

        if (nextToken.getType() == Token.TokenType.NUMBER) {
            tokenManager.matchAndRemove(Token.TokenType.NUMBER); //If token is number parses and removes it
            String value = nextToken.getValue();
            if (value.contains(".")) {
                return new FloatNode(Float.parseFloat(value)); //Creates floatnode
            } else {
                return new IntegerNode(Integer.parseInt(value)); //Otherwise creates integernode
            }
        }

        if (nextToken.getType() == Token.TokenType.MINUS) {
            tokenManager.matchAndRemove(Token.TokenType.MINUS); //If next is unary minus parses and removes it
            Node innerFactor = factor(); //Parses factor
            return new MathOpNode(null, Token.TokenType.MINUS, innerFactor); //Creates MathOpNode for unary minus
        }

        if (nextToken.getType() == Token.TokenType.LPAREN) { //If nex token is left parenthesis parses the expression
            tokenManager.matchAndRemove(Token.TokenType.LPAREN);
            Node innerExpression = expression();
            tokenManager.matchAndRemove(Token.TokenType.RPAREN); //Checks for right paren and removes it
            return innerExpression; //Returns parses expression
        }

        if (nextToken.getType() == Token.TokenType.WORD) {
            tokenManager.matchAndRemove(Token.TokenType.WORD);
            return new VariableNode(nextToken.getValue());
        }

        return null;
    }

    public static class MathOpNode extends Node {
        private final Node left;
        private final Token.TokenType operation;
        private final Node right;

        public MathOpNode(Node left, Token.TokenType operation, Node right) {
            this.left = left;
            this.operation = operation;
            this.right = right; //Initialize everything
        }

        public Node getLeft() {
            return left;
        } //Get left operand

        public Token.TokenType getOperation() {
            return operation;
        } //Get the operation type

        public Node getRight() {
            return right;
        } //Get the right operand

        @Override
        public String toString() {
            return String.format("(%s %s %s)", left, operation, right);
        } //Prints string for MathOpNode
    }

    public class ProgramNode extends Node {
        private LinkedList<StatementsNode> expressions;

        public ProgramNode(LinkedList<StatementsNode> expressions) {
            this.expressions = expressions;
        } //Initialize node

        public LinkedList<StatementsNode> getExpressions() {
            return expressions;
        } //Returns expressions

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (StatementsNode expression : expressions) {
                builder.append(expression.toString()).append(" ");
            }
            return builder.toString().trim();
        } //Prints string for programnode
    }
}
