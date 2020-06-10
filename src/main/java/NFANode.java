import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFANode {
    Map<Character, List<NFANode>> pointsTo = new HashMap<>();
    Map<Character, List<NFANode>> backwardsReference = new HashMap<>();
    boolean accepting;
    int num;

    NFANode(boolean accepting, int num) {
        this.accepting = accepting;
        this.num = num;
        pointsTo.put(null, new ArrayList<>());           //null instead of eps
    }

    void replace(NFANode replaced) {
        while (replaced.backwardsReference.entrySet().iterator().hasNext()) {
            Map.Entry<Character, List<NFANode>> pointsToReplacedEntry = replaced.backwardsReference.entrySet().iterator().next();

            while (pointsToReplacedEntry.getValue().iterator().hasNext()) {
                NFANode pointsToReplaced = pointsToReplacedEntry.getValue().iterator().next();

                pointsToReplaced.removeNode(replaced, pointsToReplacedEntry.getKey());
                pointsToReplaced.addNode(this, pointsToReplacedEntry.getKey());
            }

            replaced.backwardsReference.entrySet().remove(pointsToReplacedEntry);
        }
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

        List<NFANode> backwardNodes = node.backwardsReference.getOrDefault(moveChar, null);
        if (backwardNodes != null)
            backwardNodes.add(this);
        else {
            backwardNodes = new ArrayList<>();
            backwardNodes.add(this);
            node.backwardsReference.put(moveChar, backwardNodes);
        }
    }

    void removeNode(NFANode node, Character moveChar) {
        List<NFANode> nodes = pointsTo.get(moveChar);

        nodes.remove(node);

        List<NFANode> backwardNodes = node.backwardsReference.get(moveChar);

        backwardNodes.remove(this);
    }
}
