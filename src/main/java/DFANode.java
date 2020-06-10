import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFANode {
    List<NFANode> nodes;
    Map<Character, DFANode> pointsTo = new HashMap<>();
    Character entrantChar = null;
    boolean accepting;

    DFANode(List<NFANode> nodes) {
        this.nodes = nodes;
        for (NFANode node : nodes)
            if (node.accepting) {
                this.accepting = true;
                break;
            }
    }

    boolean equals(DFANode other) {
        boolean isEquals = true;

        for (NFANode othersNode : other.nodes) {
            boolean found = false;

            for (NFANode node : this.nodes)
                if (node.num == othersNode.num) {
                    found = true;
                    break;
                }

            if (!found) {
                isEquals = false;
                break;
            }
        }

        return isEquals && this.nodes.size() == other.nodes.size();
    }

    public void freeNFA() {
        nodes.clear();
    }

    public String toString() {
        List<Integer> nums = new ArrayList<>();
        for (NFANode node : nodes)
            nums.add(node.num);

        nums.sort(Integer::compareTo);

        return nums.size() + nums.toString() + entrantChar;
    }
}
