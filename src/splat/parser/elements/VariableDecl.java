package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.constants.types.Type;
import splat.parser.elements.expressions.LabelExpr;

public class VariableDecl extends Declaration {

	private LabelExpr label;
	private Type type;

	public VariableDecl(Token tok, LabelExpr label, Type type) {
		super(tok);
		this.label = label;
		this.type = type;
	}

	public LabelExpr getLabel() {
		return label;
	}

	public Type getType() {
		return type;
	}

	public String toString() {
		return String.format("%s : %s;", label, type);
	}
}
