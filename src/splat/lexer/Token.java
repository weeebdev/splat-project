package splat.lexer;

public class Token {
	private String value;
	private int line, column;

	public Token(String value, int line, int column) {
		this.value = value;
		this.line = line;
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return String.format("Token: \"%s\", line %d, column %d", this.value, this.line, this.column);
	}
}
