class Automaton {
    String tokenName;
    NFANode start;
    NFANode end;

    Automaton(String expression, String tokenName) throws TokenException {
        this.tokenName = tokenName;
        AutomatonPart automaton = parseExpression(expression);
        automaton.end.accepting = true;
        start = automaton.start;
        end = automaton.end;
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

    private AutomatonPart parseExpression(String expression) throws TokenException {
        if (expression.length() > 2)
            expression = removeBrackets(expression);

        boolean notFoundAnOperation = true;

        int openedBrackets = 0;
        NFANode start = new NFANode(false);
        NFANode end = new NFANode(false);

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
                        AutomatonPart alternative1 = parseExpression(expression.substring(0, i));
                        AutomatonPart alternative2 = parseExpression(expression.substring(i + 1));

                        start.addNode(alternative1.start, null);
                        start.addNode(alternative2.start, null);
                        alternative1.end.addNode(end, null);
                        alternative2.end.addNode(end, null);

                        notFoundAnOperation = false;
                    }

                    break;
                case '*':
                    if (openedBrackets == 0) {
                        AutomatonPart cycled = parseExpression(expression.substring(0, i));

                        start.addNode(cycled.start, null);
                        start.addNode(end, null);
                        cycled.end.addNode(cycled.start, null);
                        cycled.end.addNode(end, null);

                        notFoundAnOperation = false;
                    }

                    break;
                case '+':
                    if (openedBrackets == 0) {
                        AutomatonPart cycled = parseExpression(expression.substring(0, i));

                        start.addNode(cycled.start, null);
                        cycled.end.addNode(cycled.start, null);
                        cycled.end.addNode(end, null);

                        notFoundAnOperation = false;
                    }

                    break;
            }
        }

        AutomatonPart part = new AutomatonPart(start, end);

        return part;
    }
}
