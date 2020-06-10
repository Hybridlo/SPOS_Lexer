import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, TokenException {
        Tokenizer tokenizer = new Tokenizer("test.txt");
        tokenizer.printTokensToFile("res.txt");

        //some tests
        System.out.println(SymbolManager.findToken("74567876"));
        System.out.println(SymbolManager.findToken("7456.7876"));
        System.out.println(SymbolManager.findToken("alala_234_567"));

        Tokenizer tokenizer2 = new Tokenizer("test2.txt");
        tokenizer2.printTokensToFile("res2.txt");

        try {
            System.out.println(SymbolManager.findToken("_alla654567"));
        } catch (TokenException e) {
            System.out.println("Token not identified");
        }

        try {
            System.out.println(SymbolManager.findToken("Alal_1876"));
        } catch (TokenException e) {
            System.out.println("Token not identified");
        }

        try {
            System.out.println(SymbolManager.findToken("1234alal_kjh"));
        } catch (TokenException e) {
            System.out.println("Token not identified");
        }
    }
}
