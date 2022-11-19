package splat.parser.elements.literals;

import splat.lexer.Token;

public class IntLiteral extends Literal {
	private Token tok;

	public IntLiteral(Token tok) {
		super(tok);
		this.tok = tok;
	}

	public String toString() {
		return String.format("%s", tok.getValue());
	}

	/**
	 * @param value
	 * @return
	 *         checks if the value is a int literal
	 */
	public static boolean isValid(String value) {
		return Character.isDigit(value.charAt(0)) && Character.isDigit(value.charAt(value.length() - 1));
	}
}
