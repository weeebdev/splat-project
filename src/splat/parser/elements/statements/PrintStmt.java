package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

public class PrintStmt extends Statement {
	private Expression expr;

	public PrintStmt(Token tok, Expression expr) {
		super(tok);
		this.expr = expr;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("print %s;", expr != null ? expr : "");
	}
}
