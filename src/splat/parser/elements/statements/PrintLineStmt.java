package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class PrintLineStmt extends Statement {
	public PrintLineStmt(Token tok) {
		super(tok);
	}

	public String toString() {
		return String.format("print_line;");
	}
}
