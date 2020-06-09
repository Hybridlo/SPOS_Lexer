import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Tokenizer {
    private AtomicReference<String> data;
    private List<TokenException> exceptions = new ArrayList<>();
    private List<Token> tokens = new ArrayList<>();

    public Tokenizer(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream fin = new FileInputStream(file);

        byte[] byteData = new byte[(int) file.length()];

        fin.read(byteData);
        fin.close();
        data = new AtomicReference<>(new String(byteData));
    }

    private void parse() {
        exceptions = new ArrayList<>();
        tokens = new ArrayList<>();

        int index = 0;

        int row = 1;
        int column = 1;

        while (data.get().length() > 0) {
            String[] possibleOperator = SymbolManager.getOperator(data);
            if (possibleOperator != null) {
                tokens.add(new Token(possibleOperator[0], possibleOperator[1], row, column));

                column += possibleOperator[0].length();
            }

            index++;

            boolean newlineFound = false;

            String symbol = data.get().substring(0, index);

            //single token found
            if (SymbolManager.checkBreaker(symbol) || data.get().length() == symbol.length()) {
                if (SymbolManager.checkLineSeparator(data))                 //mark if breaker was a newline
                    newlineFound = true;
                else if (symbol.isBlank()) {                                //if ordinary blank not newline char - skip
                    data.set(data.get().substring(1));                      //remove said blank char
                    index--;
                    column++;
                    continue;
                }

                symbol = symbol.replaceAll("\\s+|_","");     //get symbol without whitespaces

                if (symbol.length() > 0)                                    //if we got non empty symbol
                    try {
                        String token = SymbolManager.findToken(symbol);         //get token
                        tokens.add(new Token(symbol, token, row, column));
                    } catch (TokenException e) {
                        exceptions.add(new TokenException(e.getMessage() + " at (" + row + "," + column +")"));
                    }

                data.set(data.get().substring(symbol.length()));

                column += symbol.length();

                if (newlineFound) {
                    row++;
                    column = 1;
                }

                index = 0;

                SymbolManager.checkWithLineContinuation(data);      //will remove line continuation character and newline
            }
        }

    }

    public void printTokensToFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        parse();

        String separator = System.getProperty("line.separator");

        for (Token token : tokens) {
            writer.write(token.toString() + separator);
        }

        for (TokenException e : exceptions) {
            writer.write(e.getMessage() + separator);
        }

        writer.close();
    }
}
