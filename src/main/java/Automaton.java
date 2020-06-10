import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Automaton {
    String tokenName;
    int nfaNodesAmount = 0;
    String alphabet;
    DFANode start;

    Automaton(String expression, String tokenName, String alphabet) throws TokenException {
        this.tokenName = tokenName;
        this.alphabet = alphabet.replaceAll("[|*+()]", "");
        NFAPart nfaAutomaton = expToNFA(expression);
        nfaAutomaton.end.accepting = true;
        NFANode nfaStart = nfaAutomaton.start;

        List<DFANode> dfaNodes = NFAtoDFA(nfaStart);

        for (DFANode node : dfaNodes)
            node.freeNFA();

        start = dfaNodes.get(0);
    }

    public boolean check(String word) {
        DFANode curr = start;

        for (int i = 0; i < word.length(); i++) {
            curr = curr.pointsTo.get(word.charAt(i));

            if (curr == null)
                return false;
        }

        return curr.accepting;
    }

    private void addNonrepeated(List<NFANode> to, List<NFANode> from) {
        for (NFANode toAdd : from) {
            boolean didntRepeat = true;

            for (NFANode node : to)
                if (node.num == toAdd.num) {
                    didntRepeat = false;
                    break;
                }

            if (didntRepeat) {
                to.add(toAdd);
            }
        }
    }

    private List<NFANode> epsClosAndMove(List<NFANode> before, List<NFANode> nodes, Character moveChar) {
        boolean moved = false;

        List<NFANode> res = new ArrayList<>();

        for (NFANode node : nodes) {
            for (NFANode moveNode : node.pointsTo.get(null)) {
                moved = true;
                List<NFANode> beforeAndMove = new ArrayList<>(before);
                beforeAndMove.add(moveNode);
                List<NFANode> moveNodeSingle = new ArrayList<>();
                moveNodeSingle.add(moveNode);

                addNonrepeated(res, epsClosAndMove(beforeAndMove, moveNodeSingle, moveChar));
            }

            if (moveChar != null && node.pointsTo.get(moveChar) != null && node.pointsTo.get(moveChar).size() > 0) {
                for (NFANode moveNode : node.pointsTo.get(moveChar)) {
                    moved = true;
                    List<NFANode> beforeAndMove = new ArrayList<>(before);
                    beforeAndMove.add(moveNode);
                    List<NFANode> moveNodeSingle = new ArrayList<>();
                    moveNodeSingle.add(moveNode);

                    addNonrepeated(res, epsClosAndMove(beforeAndMove, moveNodeSingle, null));
                }
            }
        }

        if (!moved && moveChar != null)
            return Collections.emptyList();

        if (!moved)
            return before;

        return res;
    }

    private List<DFANode> NFAtoDFA(NFANode nfaStart) {
        List<DFANode> dfaNodes = new ArrayList<>();

        List<NFANode> singleStart = new ArrayList<>();
        singleStart.add(nfaStart);
        DFANode start = new DFANode(epsClosAndMove(Collections.emptyList(), singleStart, null));
        start.nodes.add(nfaStart);

        dfaNodes.add(start);
        boolean foundCandidate = true;

        while (foundCandidate) {
            foundCandidate = false;
            DFANode newNode = null;
            DFANode pointsToNew = null;
            char pointsWithChar = 0;

            for (DFANode node : dfaNodes) {
                for (char moveChar : alphabet.toCharArray()) {
                    if (!node.pointsTo.containsKey(moveChar)) {
                        newNode = new DFANode(epsClosAndMove(Collections.emptyList(), node.nodes, moveChar));
                        newNode.entrantChar = moveChar;
                        foundCandidate = true;
                        pointsToNew = node;
                        pointsWithChar = moveChar;
                        break;
                    }

                }

                if (foundCandidate)
                    break;
            }

            boolean foundCopy = false;

            if (foundCandidate) {

                for (DFANode node : dfaNodes)
                    if (newNode.equals(node)) {
                        pointsToNew.pointsTo.put(pointsWithChar, node);
                        foundCopy = true;
                        break;
                    }

                if (!foundCopy) {
                    pointsToNew.pointsTo.put(pointsWithChar, newNode);
                    dfaNodes.add(newNode);
                }
            }
        }

        return dfaNodes;
    }

    private String removeBrackets(String expression) {
        int openedBrackets = 0;

        for (int i = 0; i < expression.length(); i++) {
            char currChar = expression.charAt(i);
            if (currChar == '(')
                openedBrackets++;
            if (currChar == ')')
                openedBrackets--;

            if (openedBrackets == 0 && i < expression.length() - 1)
                return expression;
        }

        return expression.substring(1, expression.length() - 1);
    }

    private List<String> breakIntoParts(String expression) {
        List<String> res = new ArrayList<>();
        int i = -1;
        int openedBrackets = 0;

        while (expression.length() > 0) {
            i++;

            if (expression.charAt(i) == '(')
                openedBrackets++;
            else if (expression.charAt(i) == ')')
                openedBrackets--;
            else if (expression.charAt(i) == '|')
                continue;

            if (openedBrackets > 0)
                continue;

            if (i + 1 < expression.length() && "|+*".contains(String.valueOf(expression.charAt(i + 1))))
                continue;

            res.add(expression.substring(0, i + 1));
            expression = expression.substring(i + 1);
            i = -1;
        }

        return res;
    }

    private NFAPart expToNFA(String expression) throws TokenException {
        if (expression.length() > 2)
            expression = removeBrackets(expression);

        boolean notFoundAnOperation = true;

        int openedBrackets = 0;
        nfaNodesAmount++;
        NFANode start = new NFANode(false, nfaNodesAmount);
        nfaNodesAmount++;
        NFANode end = new NFANode(false, nfaNodesAmount);

        List<String> parts = breakIntoParts(expression);

        if (parts.size() > 1) {

            NFAPart nfaPart = expToNFA(parts.get(0));
            nfaPart.start.replace(start);
            start = nfaPart.start;
            NFANode curr = nfaPart.end;
            parts.remove(0);

            for (String part : parts) {
                nfaPart = expToNFA(part);
                nfaPart.start.replace(curr);
                curr = nfaPart.end;
            }

            end = curr;

            return new NFAPart(start, end);
        }

        if (expression.length() == 1 && !"()|*+".contains(expression)) {
            start.addNode(end, expression.charAt(0));
        }
        else

        for (int i = 0; i < expression.length() && notFoundAnOperation; i++) {
            char currChar = expression.charAt(i);

            switch (currChar) {
                case '(':
                    openedBrackets++;
                    break;
                case ')':
                    openedBrackets--;
                    break;
                case '|':
                    if (openedBrackets == 0) {
                        List<NFAPart> alternatives = new ArrayList<>();
                        while (true) {
                            if (i == expression.length() - 1) {
                                alternatives.add(expToNFA(expression));
                                break;
                            }

                            if (openedBrackets == 0 && expression.charAt(i) == '|') {
                                alternatives.add(expToNFA(expression.substring(0, i)));
                                expression = expression.substring(i + 1);
                                i = 0;
                                continue;
                            }

                            i++;

                            if (expression.charAt(i) == '(')
                                openedBrackets++;
                            else if (expression.charAt(i) == ')')
                                openedBrackets--;
                        }

                        for (NFAPart alternative : alternatives) {
                            start.addNode(alternative.start, null);
                            alternative.end.addNode(end, null);
                        }

                        notFoundAnOperation = false;
                    }

                    break;
                case '*':
                    if (openedBrackets == 0) {
                        NFAPart cycled = expToNFA(expression.substring(0, i));

                        start.addNode(cycled.start, null);
                        cycled.end.addNode(cycled.start, null);

                        start.addNode(end, null);
                        cycled.end.addNode(end, null);

                        notFoundAnOperation = false;
                    }

                    break;
                case '+':
                    if (openedBrackets == 0) {
                        NFAPart cycled = expToNFA(expression.substring(0, i));

                        start.addNode(cycled.start, null);
                        cycled.end.addNode(cycled.start, null);
                        cycled.end.addNode(end, null);

                        notFoundAnOperation = false;
                    }

                    break;
            }
        }

        NFAPart part = new NFAPart(start, end);

        return part;
    }
}
