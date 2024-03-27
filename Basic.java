import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;

public class Basic {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please enter only ONE file name");
            System.exit(1); //If argument count is more than one, prints out message and exits
        }

        String fileName = args[0]; //Gets filename from command line
        Path myPath = Paths.get(fileName); //Makes a path object for the file

        try {
            String content = new String(Files.readAllBytes(myPath)); //Reads content of the file and turns it into a string

            Lexer lexer = new Lexer(new CodeHandler(fileName)); //Creates lexer with CodeHandler for the file
            LinkedList<Token> tokens = lexer.lex(); //Does the lex and returns a list of tokens
            Parser parser = new Parser(tokens);
            Node rootNode = parser.parse();

            for (Token token : tokens) { //Prints out each token in the form of a list
                System.out.println(token.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}