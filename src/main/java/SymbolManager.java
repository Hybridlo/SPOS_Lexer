import java.util.concurrent.atomic.AtomicReference;

public class SymbolManager {
    private SymbolManager(){}

    static void checkWithLineContinuation(AtomicReference<String> data) {
        String separator = System.getProperty("line.separator");

        if (data.get().length() < 1 + separator.length())
            return;

        if (data.get().substring(0, separator.length() + 1).equals("_" + separator))
            data.set(data.get().substring(separator.length() + 1));
    }

    static boolean checkLineSeparator(AtomicReference<String> data) {
        String separator = System.getProperty("line.separator");

        if (data.get().substring(0, separator.length()).equals(separator)) {
            data.set(data.get().substring(separator.length()));
            return true;
        }

        return false;
    }

    static boolean checkBreaker(String word) {      //check for token breakers at the end
        String check = word.substring(word.length() - 1);
        if (check.isBlank())
            return true;

        for (String breaker : SymbolTable.breakers)
            if (check.equals(breaker))
                return true;

        return false;
    }

    static String[] getOperator(AtomicReference<String> data) {
        String[] result = SymbolTable.getOperator(data.toString());
        if (result != null) {
            data.set(result[2]);    //set trimmed data
            return new String[]{result[0], result[1]};
        }

        return null;                //operator not found
    }

    static String findToken(String word) throws TokenException {
        String token = SymbolTable.symbols.getOrDefault(word, null);
        if (token != null)
            return token;
        else
            throw new TokenException("Invalid character");
    }
}
