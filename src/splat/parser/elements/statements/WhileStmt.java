package splat.parser.elements.statements;

import java.util.List;
import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;
import utils.Utils;

public class WhileStmt extends Statement {
	private Expression expr;
	private List<Statement> stmts;

	public WhileStmt(Token tok, Expression expr, List<Statement> stmts) {
		super(tok);
		this.expr = expr;
		this.stmts = stmts;
	}

	public Expression getExpr() {
		return expr;
	}

	public List<Statement> getStmt() {
		return stmts;
	}

	public String toString() {
		return String.format("while %s do %s end while;", expr, Utils.formatArray(stmts, "\n\t"));
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		if (!expr.analyzeAndGetType(funcMap, varAndParamMap).equalsTo(Type.BOOLEAN)) {
			throw new SemanticAnalysisException("While statement condition must be of type bool", this);
		}

		for (Statement stmt : stmts) {
			stmt.analyze(funcMap, varAndParamMap);
		}
	}
}
