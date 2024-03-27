import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

public abstract class Node {
    @Override
    public String toString() {
        return "";
    }
}

class IntegerNode extends Node {
    private int num;

    public IntegerNode(int num) {
        this.num = num;
    } //Constructor initializes the integer

    public int getNum() {
        return num;
    } //Method to get the integer value

    @Override
    public String toString() {
        return String.valueOf(num);
    }
}

class FloatNode extends Node {
    private float num;

    public FloatNode(float num) {
        this.num = num;
    } //Constructor initializes float

    public float getNum() {
        return num;
    } //Method to get the float value

    @Override
    public String toString() {
        return String.valueOf(num);
    }
}

class TokenManager {

    private LinkedList<Token> stream; //Token stream

    public TokenManager(LinkedList<Token> stream) {
        this.stream = stream;
    } //Initialize token stream

    public boolean MoreTokens() {
        return !stream.isEmpty();
    } //Checks if there are more tokens in the stream

    public Optional<Token> Peek(int j) {
        if (j < 0 || j >= stream.size())
            return Optional.empty(); //Checks if index is out of bonds and returns optional empty

        Iterator<Token> iterator = stream.iterator();
        while (j > 0 && iterator.hasNext()) {
            iterator.next();
            j--;
        } //Iterates through the whole stream to find token at index

        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        } else {
            return Optional.empty();
        } //If there is a token at the index returns it with optional, or returns empty optional if nothing is found
    }

    public Optional<Token> matchAndRemove(Token.TokenType t) {
        if (!stream.isEmpty()) { //If stream isnt empty checks if the head matches token type
            Token head = stream.peek();
            if (head.getType() == t) {
                stream.poll();
                return Optional.of(head); //If its the same it removes the token from the stream and returns the matched token
            }
        }
        return Optional.empty(); //If there is no match gives an empty optional
    }
}