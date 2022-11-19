package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.constants.operators.BinaryOperator;

public class BinaryOpExpr extends Expression {
	private Expression expr1;
	private Expression expr2;
	private BinaryOperator op;

	public BinaryOpExpr(Token tok, Expression expr1, Expression expr2, BinaryOperator op) {
		super(tok);
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.op = op;
	}

	public Expression getExpr1() {
		return expr1;
	}

	public Expression getExpr2() {
		return expr2;
	}

	public BinaryOperator getOp() {
		return op;
	}

	public String toString() {
		return String.format("(%s %s %s)", expr1, op, expr2);
	}
}
