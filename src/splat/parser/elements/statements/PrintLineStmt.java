package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class PrintLineStmt extends Statement {
	public PrintLineStmt(Token tok) {
		super(tok);
	}

	public String toString() {
		return String.format("print_line;");
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		// Nothing to implement
	}
}
