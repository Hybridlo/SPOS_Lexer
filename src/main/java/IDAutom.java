public class IDAutom extends Automaton {
    private static IDAutom instance;

    private IDAutom() {
        start = new DFANode(false);
        DFANode end = new DFANode(true);

        start.addNode(end, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");     //first char is a letter

        //other chars can be a letter, digit or underscore
        end.addNode(end, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_");

        tokenName = "ID";
    }

    public static IDAutom getInstance() {
        if (instance == null){
            instance = new IDAutom();
        }

        return instance;
    }
}
