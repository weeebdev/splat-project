package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.expressions.LabelExpr;

public abstract class Declaration extends ASTElement {
	private LabelExpr label;

	public Declaration(Token tok) {
		super(tok);
	}

	public LabelExpr getLabel() {
		return label;
	}

	public void setLabel(LabelExpr label) {
		this.label = label;
	}
}
