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

        boolean newlineFound = false;

        while (data.get().length() > 0) {
            String[] possibleOperator = SymbolManager.getOperator(data);
            if (possibleOperator != null) {
                tokens.add(new Token(possibleOperator[0], possibleOperator[1], row, column));

                column += possibleOperator[0].length();
            }

            SymbolManager.checkWithLineContinuation(data);

            index++;

            if (newlineFound) {
                row++;
                column = 1;
                newlineFound = false;
            }

            String symbol = data.get().substring(0, index);
            String symbolWithBreak = "";
            if (index < data.get().length())
                symbolWithBreak = data.get().substring(0, index + 1);

            //single token found
            if (data.get().length() == symbol.length() || SymbolManager.checkBreaker(symbolWithBreak) || symbol.isBlank()) {
                if (SymbolManager.checkLineSeparator(data)) {               //mark if breaker was a newline
                    newlineFound = true;
                    index = 0;
                    continue;
                }
                else if (symbol.isBlank()) {                                //if ordinary blank not newline char - skip
                    data.set(data.get().substring(1));                      //remove said blank char
                    index--;
                    column++;
                    continue;
                }

                if (symbol.length() > 0)                                    //if we got non empty symbol
                    try {
                        String token = SymbolManager.findToken(symbol);         //get token

                        if (token.equals(SymbolTable.symbols.get("\""))) {      //start string literal
                            int literalEnd = data.get().indexOf("\"", 1);       //find end
                            symbol = data.get().substring(0, literalEnd + 1);
                        }

                        if (token.equals(SymbolTable.symbols.get("\'"))) {      //start char literal
                            int literalEnd = data.get().indexOf("\'", 1);       //find end
                            symbol = data.get().substring(0, literalEnd + 1);
                        }

                        tokens.add(new Token(symbol, token, row, column));
                    } catch (TokenException e) {
                        exceptions.add(new TokenException(e.getMessage() + " at (" + row + "," + column +")"));
                    }

                data.set(data.get().substring(symbol.length()));

                column += symbol.length();

                index = 0;
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
