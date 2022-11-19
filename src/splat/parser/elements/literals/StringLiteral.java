package splat.parser.elements.literals;

import splat.lexer.Token;

public class StringLiteral extends Literal {
	private Token tok;

	public StringLiteral(Token tok) {
		super(tok);
		this.tok = tok;
	}

	public String toString() {
		return String.format("%s", tok.getValue());
	}

	/**
	 * @param value
	 * @return
	 *         checks if the value is a boolean literal
	 */
	public static boolean isValid(String value) {
		return value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"';
	}
}
