package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.constants.operators.UnaryOperator;

public class UnaryOpExp extends Expression {
	private Expression expr;
	private UnaryOperator op;

	public UnaryOpExp(Token tok, Expression expr, UnaryOperator op) {
		super(tok);
		this.expr = expr;
		this.op = op;
	}

	public Expression getExpr() {
		return expr;
	}

	public UnaryOperator getOp() {
		return op;
	}

	public String toString() {
		return String.format("(%s %s)", op, expr);
	}
}
