package splat.parser.elements.statements;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import utils.Utils;

public class IfStmt extends Statement {
	private Expression expr;
	private List<Statement> stmt1;
	private List<Statement> stmt2;

	public IfStmt(Token tok, Expression expr, List<Statement> stmt1, List<Statement> stmt2) {
		super(tok);
		this.expr = expr;
		this.stmt1 = stmt1;
		this.stmt2 = stmt2;
	}

	public Expression getExpr() {
		return expr;
	}

	public List<Statement> getStmt1() {
		return stmt1;
	}

	public List<Statement> getStmt2() {
		return stmt2;
	}

	public String toString() {
		String result = String.format("if %s then\n", expr);
		result += Utils.TAB + Utils.TAB + Utils.formatArray(stmt1, "\n" + Utils.TAB + Utils.TAB);
		if (stmt2 != null) {
			result += "\n" + Utils.TAB + "else\n";
			result += Utils.TAB + Utils.TAB + Utils.formatArray(stmt2, "\n" + Utils.TAB);
		}
		result += "\n" + Utils.TAB + "end if;";
		return result;
	}
}
