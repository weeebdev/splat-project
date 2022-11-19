package splat.parser.elements.statements;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import utils.Utils;

public class WhileStmt extends Statement {
	private Expression expr;
	private List<Statement> stmts;

	public WhileStmt(Token tok, Expression expr, List<Statement> stmts) {
		super(tok);
		this.expr = expr;
		this.stmts = stmts;
	}

	public Expression getExpr() {
		return expr;
	}

	public List<Statement> getStmt() {
		return stmts;
	}

	public String toString() {
		return String.format("while %s do %s end while;", expr, Utils.formatArray(stmts, "\n\t"));
	}
}
