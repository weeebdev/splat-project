package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.constants.types.Type;
import splat.parser.elements.expressions.LabelExpr;

public class VariableDecl extends Declaration {

	private Type type;

	public VariableDecl(Token tok, LabelExpr label, Type type) {
		super(tok);
		super.setLabel(label);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public String toString() {
		return String.format("%s : %s;", super.getLabel(), type);
	}
}
