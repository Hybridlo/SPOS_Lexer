public class Token {
    private String symbol;
    private String name;
    private int row;
    private int column;

    Token(String symbol, String name, int row, int column) {
        this.symbol = symbol;
        this.name = name;
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return "{ \"" + symbol + "\", " + name + ", (" + row + "," + column + ") }";
    }
}
