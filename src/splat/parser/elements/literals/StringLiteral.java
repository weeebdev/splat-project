package splat.parser.elements.literals;

import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.executor.values.ValueFac;
import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class StringLiteral extends Literal {
	private Token tok;

	public StringLiteral(Token tok) {
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
		return value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"';
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		return new Type(Type.STRING);
	}

	@Override
	public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ExecutionException {
		return ValueFac.createValue(Type.STRING, tok.getValue().replaceAll("\"", ""));
	}
}
