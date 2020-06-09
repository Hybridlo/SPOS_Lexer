public class Token {
    String symbol;
    String name;
    int row;
    int column;

    Token(String symbol, String name, int row, int column) {
        this.symbol = symbol;
        this.name = name;
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return "{" + name + ", " + symbol + ", (" + row + "," + column + ")}";
    }
}
