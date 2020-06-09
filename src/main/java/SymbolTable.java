import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private SymbolTable(){}

    public String digits = "0123456789";
    public String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    public String UpperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static Map<String, String> init() {
        Map<String, String> res = new HashMap<>();

        res.put("(", "SEP_OPEN_BRACKET");           //separators
        res.put(")", "SEP_CLOSE_BRACKET");
        res.put("{", "SEP_OPEN_CURLY_BRACKET");
        res.put("}", "SEP_CLOSE_CURLY_BRACKET");
        res.put("!", "SEP_EXCLAMATION");
        res.put("#", "SEP_SHARP");
        res.put(",", "SEP_COMMA");
        res.put(".", "SEP_DOT");
        res.put(":", "SEP_COLON");
        res.put(":=", "SEP_COLON_EQ");
        res.put("?", "SEP_QUESTION");

        res.put("If", "KEY_IF");                    //keywords
        res.put("Else", "KEY_ELSE");
        res.put("Dim", "KEY_DIM");
        res.put("For", "KEY_FOR");
        res.put("Or", "KEY_OR");
        res.put("Exit", "KEY_EXIT");
        res.put("Option", "KEY_OPTION");
        res.put("On", "KEY_ON");
        res.put("Module", "KEY_MODULE");
        res.put("Sub", "KEY_SUB");
        res.put("As", "KEY_AS");
        res.put("To", "KEY_To");
        res.put("Step", "KEY_STEP");
        res.put("Next", "KEY_NEXT");
        res.put("End", "KEY_END");

        res.put("Integer", "TYPE_INTEGER");         //primitive types
        res.put("Double", "TYPE_DOUBLE");
        res.put("Boolean", "TYPE_BOOLEAN");
        res.put("String", "TYPE_STRING");
        res.put("Date", "TYPE_DATE");
        res.put("Char", "TYPE_CHAR");

        res.put("True", "LTR_BOOL_TRUE");           //literals
        res.put("False", "LTR_BOOL_FALSE");
        res.put("\"", "LTR_STRING_QUOTE");
        res.put("\'", "LTR_CHAR_QUOTE");

        res.put("@int_automaton", "LTR_INTEGER");   //complex literals
        res.put("@double_automaton", "LTR_DOUBLE");

        res.put("@ID", "ID");                       //IDs

        return res;
    }

    public static Map<String, String> symbols = init();
}
