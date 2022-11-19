package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.expressions.LabelExpr;

public class AssignmentStmt extends Statement {
	private LabelExpr label;
	private Expression expr;

	public AssignmentStmt(Token tok, LabelExpr label, Expression expr) {
		super(tok);
		this.label = label;
		this.expr = expr;
	}

	public LabelExpr getLabel() {
		return label;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("%s := %s;", label, expr);
	}
}
