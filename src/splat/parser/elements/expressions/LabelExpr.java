package splat.parser.elements.expressions;

import java.util.Arrays;
import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.constants.keyword.Keyword;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class LabelExpr extends Expression {
	private Token tok;

	public LabelExpr(Token tok) {
		super(tok);
		this.tok = tok;
	}

	public String toString() {
		return String.format("%s", tok.getValue());
	}

	public Token getTok() {
		return tok;
	}

	public static boolean isValid(String value) {
		return !Arrays.asList(Keyword.getKeywords()).contains(value) && (value.charAt(0) == '_'
				|| Character.isLetter(value.charAt(0)));
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		Type type = varAndParamMap.get(tok.getValue());
		if (type == null) {
			throw new SemanticAnalysisException(String.format("Variable %s is not declared", tok.getValue()),
					tok.getLine(), tok.getColumn());
		}
		return type;
	}

	@Override
	public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ExecutionException {
		return varAndParamMap.get(tok.getValue());
	}
}
