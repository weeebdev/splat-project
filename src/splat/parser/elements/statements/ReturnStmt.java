package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.constants.types.RetType;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class ReturnStmt extends Statement {
	private Expression expr;

	public ReturnStmt(Token tok, Expression expr) {
		super(tok);
		this.expr = expr;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("return %s;", expr != null ? expr : "");
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		Type returnType = varAndParamMap.get("return");
		if (returnType == null) {
			throw new SemanticAnalysisException("Return statement outside function", this);
		}

		if (expr == null) {
			if (returnType.equalsTo(RetType.VOID)) {
				return;
			} else {
				throw new SemanticAnalysisException("Return statement without value", this);
			}
		}

		Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);
		if (!returnType.equalsTo(exprType)) {
			throw new SemanticAnalysisException("Type mismatch in return statement", this);
		}
	}
}
