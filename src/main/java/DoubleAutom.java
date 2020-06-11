public class DoubleAutom extends Automaton {
    private static DoubleAutom instance;

    private DoubleAutom() {
        start = new DFANode(false);
        DFANode digitsBefore = new DFANode(false);

        start.addNode(digitsBefore, "123456789");                 //first digit is needed, not 0

        digitsBefore.addNode(digitsBefore, "0123456789");         //next can be any amount of digits

        DFANode dot = new DFANode(false);                          //must have a dot

        digitsBefore.addNode(dot, ".");                           //move over a dot

        DFANode digitsAfter = new DFANode(true);                   //at least 1 digit necessary

        dot.addNode(digitsAfter, "0123456789");                   //move to first digit after

        digitsAfter.addNode(digitsAfter, "0123456789");           //after dot any amount of any digits allowed

        tokenName = "LTR_DOUBLE";
    }

    public static DoubleAutom getInstance() {
        if (instance == null){
            instance = new DoubleAutom();
        }

        return instance;
    }
}
