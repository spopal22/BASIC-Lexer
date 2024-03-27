public class FORNode extends StatementNode {
    private String loop;
    private int start;
    private int end;
    private int step;

    public FORNode(String loop, int start, int end) {
        this.loop = loop;
        this.start = start;
        this.end = end;
        this.step = 1;
    }

    public FORNode(String loop, int start, int end, int step) {
        this.loop = loop;
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public String getLoop() {
        return loop;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getStep() {
        return step;
    }

    @Override
    public String toString() {
        if (step == 1) {
            return "The FOR loop is: " + loop + " from " + start + " to " + end;
        } else {
            return "The FOR loop is: " + loop + " from " + start + " to " + end + " (the step is: " + step + ")";
        }
    }
}