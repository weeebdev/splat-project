package splat.parser.elements.expressions;

import java.util.Arrays;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.constants.keyword.Keyword;

public class LabelExpr extends Expression {
	private Token tok;

	public LabelExpr(Token tok) {
		super(tok);
		this.tok = tok;
	}

	public String toString() {
		return String.format("%s", tok.getValue());
	}

	public Token getTok() {
		return tok;
	}

	public static boolean isValid(String value) {
		return !Arrays.asList(Keyword.getKeywords()).contains(value) && (value.charAt(0) == '_'
				|| Character.isLetter(value.charAt(0)));
	}
}
