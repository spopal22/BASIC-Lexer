import java.io.IOException;
import java.nio.file.*;

public class CodeHandler {
    private int index;
    private String data; //Index to keep track of our position in code and string to store the data

    public CodeHandler(String filename) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
        this.data = new String(fileBytes);
        this.index = 0;
    } //Converts the file content to a string and reads all the bytes. Initializes index at 0

    public char peek(int i) {
        int peekAhead = index + i;
        if (peekAhead < data.length()) {
            return data.charAt(peekAhead);  //Makes sure when we peek its within the range of data and returns that char
        } else {
            return '\0'; //Otherwise gives us null
        }
    }

    public String peekString(int i) {
        int peekStringEndIndex = Math.min(index + i, data.length());
        return data.substring(index, peekStringEndIndex);
    }  //Peeks ahead by specified amount and gives all the data for what we peeked

    public char getChar() {
        char nextChar = data.charAt(index);
        index++;
        return nextChar;
    } //Returns the next char and increases index by one

    public void swallow(int i) {
        index = Math.min(index + i, data.length());
    } //Increases the index by specified amount

    public boolean isDone() {
        return index >= data.length();
    } //Checks to see if the code is done by seeing if the index is at the end

    public String remainder() {
        if (index < data.length()) {
            return data.substring(index);
        } else { //Returns the remaining characters based off our current index position
            return ""; //Returns white space if we're already at the end of the index
        }
    }
}