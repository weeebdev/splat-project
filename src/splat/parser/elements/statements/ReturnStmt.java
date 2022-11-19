package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

public class ReturnStmt extends Statement {
	private Expression expr;

	public ReturnStmt(Token tok, Expression expr) {
		super(tok);
		this.expr = expr;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("return %s;", expr != null ? expr : "");
	}
}
