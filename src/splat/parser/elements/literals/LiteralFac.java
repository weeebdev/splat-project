package splat.parser.elements.literals;

import splat.lexer.Token;

public class LiteralFac {
	public static Literal createLiteral(Token tok) {
		String value = tok.getValue();
		if (StringLiteral.isValid(value)) {
			return new StringLiteral(tok);
		} else if (BoolLiteral.isValid(value)) {
			return new BoolLiteral(tok);
		} else if (IntLiteral.isValid(value)) {
			return new IntLiteral(tok);
		}

		return null;
	}

	public static boolean isValid(String value) {
		return StringLiteral.isValid(value) || BoolLiteral.isValid(value) || IntLiteral.isValid(value);
	}
}
