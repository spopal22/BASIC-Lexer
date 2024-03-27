public class NEXTNode extends StatementNode {
    private String loop;

    public NEXTNode(String loop) {
        this.loop = loop;
    }

    public String getLoop() {
        return loop;
    }

    @Override
    public String toString() {
        return "The NEXT node is: " + loop;
    }
}