import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private SymbolTable(){}

    public static String DIGITS = "0123456789";
    public static String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String[] breakers = {"(", ")", "{", "}", "!", "#", ",", ".", ":", "?",       //all separator
            ">", "<", "=", "+", "-", "*", "/", "\\", "^", "&", "=", "_"};                      //and operator starting symbols

    static Map<String, String> symbols = init();
    static Map<String, String> complexSymbols = init2();

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

        return res;
    }

    private static Map<String, String> init2() {
        Map<String, String> res = new HashMap<>();

        res.put("@int_automaton", "LTR_INTEGER");   //complex literals
        res.put("@double_automaton", "LTR_DOUBLE");

        res.put("@ID_automaton", "ID");                       //IDs

        return res;
    }

    static String[] getOperator(String word) {
        int state = 0;
        int index = 0;
        char c;

        while (true) {
            switch (state) {
                case 0:
                    c = word.charAt(index);
                    index++;
                    if (c == '+') state = 1;
                    else if (c == '-') state = 2;
                    else if (c == '*') state = 3;
                    else if (c == '/') state = 4;
                    else if (c == '\\') state = 5;
                    else if (c == '^') state = 6;
                    else if (c == '&') state = 7;
                    else if (c == '=') state = 8;
                    else if (c == '<') state = 9;
                    else if (c == '>') state = 10;
                    else if (c == ':') state = 13;      //exception to the rule, for separators : and :=
                    else return null;
                    break;

                case 1:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"+", "OP_PLUS", word.substring(1)};            //end of word

                    if (c == '=')
                        return new String[]{"+=", "OP_PLUS_EQ", word.substring(2)};         //+= got
                    else
                        return new String[]{"+", "OP_PLUS", word.substring(1)};             //+ go

                case 2:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"-", "OP_MINUS", word.substring(1)};           //end of word

                    if (c == '=')
                        return new String[]{"-=", "OP_MINUS_EQ", word.substring(2)};        //-= got
                    else
                        return new String[]{"-", "OP_MINUS", word.substring(1)};            //- got

                case 3:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"*", "OP_MULT", word.substring(1)};            //end of word

                    if (c == '=')
                        return new String[]{"*=", "OP_MULT_EQ", word.substring(2)};         //*= got
                    else
                        return new String[]{"*", "OP_MULT", word.substring(1)};             //* got

                case 4:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"/", "OP_DIV", word.substring(1)};             //end of word

                    if (c == '=')
                        return new String[]{"/=", "OP_DIV_EQ", word.substring(2)};          // /= got
                    else
                        return new String[]{"/", "OP_DIV", word.substring(1)};              // / got

                case 5:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"\\", "OP_DIV_INT", word.substring(1)};        //end of word

                    if (c == '=')
                        return new String[]{"\\=", "OP_DIV_INT_EQ", word.substring(2)};     // \= got
                    else
                        return new String[]{"\\", "OP_DIV_INT", word.substring(1)};         // \ got

                case 6:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"^", "OP_POW", word.substring(1)};             //end of word

                    if (c == '=')
                        return new String[]{"^=", "OP_POW_EQ", word.substring(2)};          //^= got
                    else
                        return new String[]{"^", "OP_POW", word.substring(1)};              //^ got

                case 7:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"&", "OP_CONCAT", word.substring(1)};          //end of word

                    if (c == '=')
                        return new String[]{"&=", "OP_CONCAT_EQ", word.substring(2)};       //&= got
                    else
                        return new String[]{"&", "OP_CONCAT", word.substring(1)};           //& got

                case 8:
                    return new String[]{"=", "OP_EQ", word.substring(1)};                   //= got

                case 9:
                    if (index < word.length()) {
                        c = word.charAt(index);
                        index++;
                    } else return new String[]{"<", "OP_LT", word.substring(1)};            //end of word

                    if (c == '<') state = 11;
                    else if (c == '=') return new String[]{"<=", "OP_LT_EQ", word.substring(2)};    //<= got
                    else if (c == '>') return new String[]{"<>", "OP_NOT_EQ", word.substring(2)};   //<> got
                    else return new String[]{"<", "OP_LT", word.substring(1)};              //< got

                    break;

                case 10:
                    if (index < word.length()) {
                        c = word.charAt(index);
                        index++;
                    } else return new String[]{">", "OP_GT", word.substring(1)};            //end of word

                    if (c == '>') state = 12;
                    else if (c == '=') return new String[]{">=", "OP_GT_EQ", word.substring(2)};    //>= got
                    else return new String[]{">", "OP_GT", word.substring(1)};              //> got

                    break;

                case 11:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{"<<", "OP_SFT_LEFT", word.substring(2)};       //end of word

                    if (c == '=')
                        return new String[]{"<<=", "OP_SFT_LEFT_EQ", word.substring(3)};    //<<= got
                    else
                        return new String[]{"<<", "OP_SFT_LEFT", word.substring(2)};        //<< got

                case 12:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{">>", "OP_SFT_RIGHT", word.substring(2)};      //end of word

                    if (c == '=')
                        return new String[]{">>=", "OP_SFT_RIGHT_EQ", word.substring(3)};   //<<= got
                    else
                        return new String[]{">>", "OP_SFT_RIGHT", word.substring(2)};       //<< got

                case 13:
                    if (index < word.length())
                        c = word.charAt(index);
                    else return new String[]{":", "SEP_COLON", word.substring(1)};          //end of word

                    if (c == '=')
                        return new String[]{":=", "SEP_COLON_EQ", word.substring(2)};       //:= got
                    else
                        return new String[]{":", "SEP_COLON", word.substring(1)};           //: got
            }
        }
    }
}
