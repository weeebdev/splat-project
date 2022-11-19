package splat.parser.elements.statements;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.expressions.LabelExpr;
import utils.Utils;

public class CallStmt extends Statement {
	private LabelExpr label;
	private List<Expression> args;

	public CallStmt(Token tok, LabelExpr label, List<Expression> args) {
		super(tok);
		this.label = label;
		this.args = args;
	}

	public LabelExpr getLabel() {
		return label;
	}

	public List<Expression> getArgs() {
		return args;
	}

	public String toString() {
		return String.format("%s(%s);", label, Utils.formatArray(args, ", "));
	}

}
