import java.util.Map;

public abstract class Automaton {
    DFANode start;
    String tokenName;

    boolean check(String word) {
        DFANode curr = start;

        for (int i = 0; i < word.length(); i++) {                   //go through each char in word
            boolean moved = false;

            for (Map.Entry<String, DFANode> entry : curr.pointsTo.entrySet()) {
                String key = entry.getKey();

                if (key.indexOf(word.charAt(i)) > -1) {             //take 1 character, check if it's in key
                    curr = entry.getValue();                        //move
                    moved = true;
                    break;                                          //start checking next char
                }
            }

            if (!moved)                 //if no movement was made then character does not belong to this automaton at this point
                return false;
        }

        return curr.accepting;
    }

    String getTokenName() {
        return tokenName;
    }
}
