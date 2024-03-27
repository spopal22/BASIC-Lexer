public class GOSUBNode extends StatementNode {
    private final String identifier;

    public GOSUBNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "The GOSUB node is: " + identifier;
    }
}