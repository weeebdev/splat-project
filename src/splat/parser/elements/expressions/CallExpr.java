package splat.parser.elements.expressions;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import utils.Utils;

public class CallExpr extends Expression {
	private LabelExpr label;
	private List<Expression> args;

	public CallExpr(Token tok, LabelExpr label, List<Expression> args) {
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
		return String.format("%s(%s)", label, Utils.formatArray(args, ", "));
	}

}
