import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFANode {
    Map<String, DFANode> pointsTo = new HashMap<>();
    boolean accepting;

    DFANode(boolean accepting) {
        this.accepting = accepting;
    }

    public void addNode(DFANode node, String moveChars) {
        pointsTo.put(moveChars, node);
    }
}
