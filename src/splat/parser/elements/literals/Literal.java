package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.Expression;

public abstract class Literal extends Expression {
	public Literal(Token tok) {
		super(tok);
	}
}