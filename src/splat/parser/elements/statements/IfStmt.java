package splat.parser.elements.statements;

import java.util.List;
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
import utils.Utils;

public class IfStmt extends Statement {
	private Expression expr;
	private List<Statement> stmt1;
	private List<Statement> stmt2;

	public IfStmt(Token tok, Expression expr, List<Statement> stmt1, List<Statement> stmt2) {
		super(tok);
		this.expr = expr;
		this.stmt1 = stmt1;
		this.stmt2 = stmt2;
	}

	public Expression getExpr() {
		return expr;
	}

	public List<Statement> getStmt1() {
		return stmt1;
	}

	public List<Statement> getStmt2() {
		return stmt2;
	}

	public String toString() {
		String result = String.format("if %s then\n", expr);
		result += Utils.TAB + Utils.TAB + Utils.formatArray(stmt1, "\n" + Utils.TAB + Utils.TAB);
		if (stmt2 != null) {
			result += "\n" + Utils.TAB + "else\n";
			result += Utils.TAB + Utils.TAB + Utils.formatArray(stmt2, "\n" + Utils.TAB);
		}
		result += "\n" + Utils.TAB + "end if;";
		return result;
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		if (!expr.analyzeAndGetType(funcMap, varAndParamMap).equalsTo(Type.BOOLEAN)) {
			throw new SemanticAnalysisException("If statement condition must be of type bool", this);
		}

		for (Statement stmt : stmt1) {
			stmt.analyze(funcMap, varAndParamMap);
		}

		if (stmt2 != null) {
			for (Statement stmt : stmt2) {
				stmt.analyze(funcMap, varAndParamMap);
			}
		}
	}

	@Override
	public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ReturnFromCall, ExecutionException {
		boolean b = (boolean) expr.evaluate(funcMap, varAndParamMap).getValue();
		if (b) {
			for (Statement stmt : stmt1) {
				stmt.execute(funcMap, varAndParamMap);
			}
		} else {
			if (stmt2 != null) {
				for (Statement stmt : stmt2) {
					stmt.execute(funcMap, varAndParamMap);
				}
			}
		}
	}
}
