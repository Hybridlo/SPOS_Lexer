import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Tokenizer tokenizer = new Tokenizer("test.txt");
        tokenizer.printTokensToFile("res.txt");
    }
}
