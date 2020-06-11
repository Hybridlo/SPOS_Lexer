public class IntegerAutom extends Automaton {
    private static IntegerAutom instance;

    private IntegerAutom() {
        start = new DFANode(false);
        DFANode end = new DFANode(true);

        start.addNode(end, "123456789");        //first digit is needed, can't start from 0

        end.addNode(end, "0123456789");         //any other symbol must be digit

        tokenName = "LTR_INTEGER";
    }

    public static IntegerAutom getInstance() {
        if (instance == null){
            instance = new IntegerAutom();
        }

        return instance;
    }
}
