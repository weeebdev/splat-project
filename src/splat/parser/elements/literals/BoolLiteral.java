package splat.parser.elements.literals;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class BoolLiteral extends Literal {
	private Token tok;

	public BoolLiteral(Token tok) {
		super(tok);
		this.tok = tok;
	}

	public String toString() {
		return String.format("%s", tok.getValue());
	}

	/**
	 * @param value
	 * @return
	 *         checks if the value is a boolean literal
	 */
	public static boolean isValid(String value) {
		return value.equals("true") || value.equals("false");
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		return new Type(Type.BOOLEAN);
	}
}
