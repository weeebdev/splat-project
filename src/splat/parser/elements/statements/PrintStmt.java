package splat.parser.elements.statements;

import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class PrintStmt extends Statement {
	private Expression expr;

	public PrintStmt(Token tok, Expression expr) {
		super(tok);
		this.expr = expr;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("print %s;", expr != null ? expr : "");
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		expr.analyzeAndGetType(funcMap, varAndParamMap);
	}

	@Override
	public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ReturnFromCall, ExecutionException {
		Value val = expr.evaluate(funcMap, varAndParamMap);
		System.out.print(val);
	}
}
