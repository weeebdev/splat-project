package splat.parser.elements.constants.keyword;

import java.util.stream.Stream;

public enum Keyword implements IKeyword {
	SEMICOLON(";"),
	COLON(":"),
	OPEN_PAREN("("),
	CLOSE_PAREN(")"),
	COMMA(","),
	ASSIGN(":="),
	WHILE("while"),
	IF("if"),
	ELSE("else"),
	THEN("then"),
	PRINT("print"),
	PRINT_LINE("print_line"),
	IS("is"),
	DO("do"),
	RETURN("return"),
	BEGIN("begin"),
	END("end");

	private String name;

	private Keyword(String name) {
		this.name = name;
	}

	public static String[] getKeywords() {
		return Stream.of(values()).map(t -> t.toString()).toArray(String[]::new);
	}

	public String toString() {
		return name;
	}
}
