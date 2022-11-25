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
import splat.parser.elements.expressions.LabelExpr;
import splat.semanticanalyzer.SemanticAnalysisException;

public class AssignmentStmt extends Statement {
	private LabelExpr label;
	private Expression expr;

	public AssignmentStmt(Token tok, LabelExpr label, Expression expr) {
		super(tok);
		this.label = label;
		this.expr = expr;
	}

	public LabelExpr getLabel() {
		return label;
	}

	public Expression getExpr() {
		return expr;
	}

	public String toString() {
		return String.format("%s := %s;", label, expr);
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		Type labelType = label.analyzeAndGetType(funcMap, varAndParamMap);
		Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

		if (!labelType.equalsTo(exprType)) {
			throw new SemanticAnalysisException("Type mismatch in assignment statement", this);
		}
	}

	@Override
	public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ReturnFromCall, ExecutionException {
		Value val = expr.evaluate(funcMap, varAndParamMap);
		varAndParamMap.put(label.getTok().getValue(), val);
	}
}
