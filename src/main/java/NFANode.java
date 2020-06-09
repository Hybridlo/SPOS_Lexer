import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFANode {
    Map<Character, List<NFANode>> pointsTo = new HashMap<>();
    boolean accepting;

    NFANode(boolean accepting) {
        this.accepting = accepting;
        pointsTo.put(null, new ArrayList<>());           //null instead of eps
    }

    void addNode(NFANode node, Character moveChar) {
        List<NFANode> nodes = pointsTo.getOrDefault(moveChar, null);
        if (nodes != null)
            nodes.add(node);
        else {
            nodes = new ArrayList<>();
            nodes.add(node);
            pointsTo.put(moveChar, nodes);
        }
    }

    void removeNode(NFANode node, Character moveChar) {
        List<NFANode> nodes = pointsTo.get(moveChar);

        nodes.remove(node);
    }
}
