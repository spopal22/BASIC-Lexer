import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {
    int lineNumber;
    int pos;
    private final CodeHandler codeHandler; //Tracks line number and position. Also references CodeHandler
    private final HashMap<String, Token.TokenType> tokenMap;
    private HashMap<String, Token.TokenType> oneChar;
    private final HashMap<String, Token.TokenType> twoChar; //Various hashmaps for the tokens, onechar methods, and twochar methods

    public Lexer(CodeHandler codeHandler) {
        this.lineNumber = 1;
        this.pos = 1;
        this.codeHandler = codeHandler;
        this.tokenMap = new HashMap<>();
        this.oneChar = new HashMap<>();
        this.twoChar = new HashMap<>();
        TokenMap();
        oneChar();
        twoChar();
    } //Initializes the line number, position, codeHandler, and hashmaps

    private void TokenMap() {
        tokenMap.put("print", Token.TokenType.PRINT);
        tokenMap.put("read", Token.TokenType.READ);
        tokenMap.put("input", Token.TokenType.INPUT);
        tokenMap.put("data", Token.TokenType.DATA);
        tokenMap.put("gosub", Token.TokenType.GOSUB);
        tokenMap.put("for", Token.TokenType.FOR);
        tokenMap.put("to", Token.TokenType.TO);
        tokenMap.put("step", Token.TokenType.STEP);
        tokenMap.put("next", Token.TokenType.NEXT);
        tokenMap.put("return", Token.TokenType.RETURN);
        tokenMap.put("if", Token.TokenType.IF);
        tokenMap.put("then", Token.TokenType.THEN);
        tokenMap.put("function", Token.TokenType.FUNCTION);
        tokenMap.put("while", Token.TokenType.WHILE);
        tokenMap.put("end", Token.TokenType.END);
    } //Adds all the keywords to their corresponding token type

    private void oneChar() {
        oneChar = new HashMap<>();
        oneChar.put("=", Token.TokenType.EQUALS);
        oneChar.put("<", Token.TokenType.LESSER);
        oneChar.put(">", Token.TokenType.GREATER);
        oneChar.put("(", Token.TokenType.LPAREN);
        oneChar.put(")", Token.TokenType.RPAREN);
        oneChar.put("+", Token.TokenType.PLUS);
        oneChar.put("-", Token.TokenType.MINUS);
        oneChar.put("*", Token.TokenType.MULTIPLY);
        oneChar.put("/", Token.TokenType.DIVIDE);
        oneChar.put(",", Token.TokenType.COMMA);
        oneChar.put(".", Token.TokenType.PERIOD);
        oneChar.put(":", Token.TokenType.COLON);
    }  //Adds all the keywords to their corresponding token type
    private void twoChar() {
        twoChar.put("<=", Token.TokenType.LESSEROR);
        twoChar.put(">=", Token.TokenType.GREATEROR);
        twoChar.put("<>", Token.TokenType.NOTEQUALS);
        twoChar.put("==", Token.TokenType.DOUBLEEQUALS);
        twoChar.put("&&", Token.TokenType.AND);
        twoChar.put("++", Token.TokenType.INCREMENT);
        twoChar.put("--", Token.TokenType.DECREMENT);
    }  //Adds all the keywords to their corresponding token type

    public LinkedList<Token> lex() {
        LinkedList<Token> tokens = new LinkedList<>(); //Initializes linked list to store the tokens

        while (!codeHandler.isDone()) { //Keeps on processing until code is done
            char currentChar = codeHandler.peek(0); //Peeks ahead without moving the index

            if (currentChar == '\r') {
                codeHandler.swallow(1); //Skips over \r
            } else {
                if (currentChar == ' ' || currentChar == '\t') {
                    codeHandler.swallow(1);
                    pos++; //Skips over any white space and updates position accordingly
                } else if (currentChar == '\n') {
                    tokens.add(new Token(Token.TokenType.ENDOFLINE, lineNumber, pos, null));
                    pos = 0;
                    lineNumber++;
                    codeHandler.swallow(1); //IF end of line character is detected, creates appropriate token, and updates the position and line number
                } else if (Character.isLetter(currentChar) || currentChar == '_' || currentChar == '$' || currentChar == '%') {
                    StringBuilder wordBuilder = new StringBuilder();
                    while (Character.isLetterOrDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '_' || codeHandler.peek(0) == '$' || codeHandler.peek(0) == '%') {
                        wordBuilder.append(codeHandler.getChar());
                        pos++;
                    }
                    String word = wordBuilder.toString().toLowerCase();

                    if (codeHandler.peek(0) == ':') {
                        tokens.add(new Token(Token.TokenType.LABEL, lineNumber, pos, word));
                        tokens.add(new Token(Token.TokenType.COLON, lineNumber, pos + 1, ":"));
                        codeHandler.swallow(1);
                    } else {
                        Token.TokenType tokenType = tokenMap.getOrDefault(word.toLowerCase(), Token.TokenType.WORD);
                        if (tokenType != Token.TokenType.WORD) {
                            tokens.add(new Token(tokenType, lineNumber, pos, null));
                        } else {
                            tokens.add(new Token(Token.TokenType.WORD, lineNumber, pos, word));
                        }
                    }
                } else if (Character.isDigit(currentChar)) {
                    Token digitToken = processNumber();
                    tokens.add(digitToken); //If there are any digits it adds it as a number token
                } else if (currentChar == '"') {
                    if (codeHandler.peek(1) == '"') { //Checks to see if the next character is also "
                        tokens.add(new Token(Token.TokenType.STRINGLITERAL, lineNumber, pos, ""));
                        codeHandler.swallow(2); //If it is, adds stringlateral token and swallows both "
                    } else {
                        Token stringLiteralToken = handleStringLiteral();
                        tokens.add(stringLiteralToken); //If not followed by another ", handles the string normally
                    }
                } else if (currentChar == '\\' && codeHandler.peek(1) == '"') { //For escape double quotes generates token accordingly
                    tokens.add(new Token(Token.TokenType.STRINGLITERAL, lineNumber, pos, ""));
                    codeHandler.swallow(2);
                } else if (twoChar.containsKey(codeHandler.peekString(2))) {
                    String twoCharToken = codeHandler.peekString(2); //Checks next two characters to see if it makes one of the twochar tokens
                    tokens.add(new Token(twoChar.get(twoCharToken), lineNumber, pos, twoCharToken));
                    codeHandler.swallow(2);
                    pos += 2; //If it does, adds the token and swallows the characters
                } else if (oneChar.containsKey(String.valueOf(currentChar))) { //Checks current character to see if its a onechar toekn
                    tokens.add(new Token(oneChar.get(String.valueOf(currentChar)), lineNumber, pos, String.valueOf(currentChar)));
                    codeHandler.swallow(1);
                    pos++; //If it is, adds the token and swallows the character

                } else if (currentChar == ',') { //Checks if currentchar is a comma
                    tokens.add(new Token(Token.TokenType.COMMA, lineNumber, pos, ","));
                    codeHandler.swallow(1);
                    pos++; //If it is, creates token, swallows, and increments position
                } else if (currentChar == '.') { //Checks if currentchar is a period
                    tokens.add(new Token(Token.TokenType.PERIOD, lineNumber, pos, "."));
                    codeHandler.swallow(1);
                    pos++; //If it is, creates token, swallows, and increments position
                } else {
                    throw new RuntimeException("Unrecognizable character detected: " + currentChar);
                }
            }
        }
        if (tokens.isEmpty() || tokens.getLast().getType() != Token.TokenType.ENDOFLINE) {
            tokens.add(new Token(Token.TokenType.ENDOFLINE, lineNumber, pos, null));
        } //Generates ENDOFLINE token if line is empty, file is blank, and at the end of a line
        return tokens;
    }

    public Token handleStringLiteral() {
        StringBuilder stringLiteralBuilder = new StringBuilder();
        codeHandler.swallow(1); //Initializes stringbuilder to make the string literal and skips the first "

        while(!codeHandler.isDone()){ //Loops until end of string is reached
            char currentChar = codeHandler.getChar();

            if (currentChar == '\\') { //Checks if current char is escape char
                if (codeHandler.peek(0) == '"') {
                    stringLiteralBuilder.append('"');
                    codeHandler.swallow(1); //If next char is " adds " to end of string and swallows
                } else {
                    stringLiteralBuilder.append(currentChar); //Or appends escape char to itself
                }
            } else if (currentChar == '"') {
                break; //If current char is the " that closes the double quotes, exits loop
            } else {
                stringLiteralBuilder.append(currentChar); //Appends current char to the string
            }
        }
        return new Token(Token.TokenType.STRINGLITERAL, lineNumber, pos, stringLiteralBuilder.toString()); //Creates new token for string literal and returns it
    }


    public Token processWord() {
        StringBuilder wordBuilder = new StringBuilder();
        while (Character.isLetterOrDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '_'
                || codeHandler.peek(0) == '$' || codeHandler.peek(0) == '%') {
            wordBuilder.append(codeHandler.getChar());
            pos++; //Appends all words until it comes across a symbol and increases the position
        }
        String word = wordBuilder.toString().toLowerCase();
        Token.TokenType tokenType = tokenMap.getOrDefault(word.toLowerCase(), Token.TokenType.WORD);
        if (tokenType != Token.TokenType.WORD){
            return new Token(tokenType, lineNumber, pos, null);
        } else {
            return new Token(Token.TokenType.WORD, lineNumber, pos, word);
        }
    } //Returns a new token of the word that was just appended

    public Token processNumber() {
        StringBuilder numberBuilder = new StringBuilder();

        while (Character.isDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '.') {
            numberBuilder.append(codeHandler.getChar());
            pos++; //Appends all numbers until a non digit is met and increases position
        }

        return new Token(Token.TokenType.NUMBER, lineNumber, pos, numberBuilder.toString());
    } //Stores that number as a NUMBER token
}
