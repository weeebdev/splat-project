package splat.parser.elements.constants.operators;

import java.util.stream.Stream;

import splat.parser.elements.constants.keyword.IKeyword;

public enum BinaryOperator implements IKeyword {
	AND("and"),
	OR("or"),
	GT(">"),
	LT("<"),
	EQ("=="),
	GTE(">="),
	LTE("<="),
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	MOD("%");

	private String name;

	private BinaryOperator(String name) {
		this.name = name;
	}

	public static BinaryOperator getEnum(String name) {
		return Stream.of(BinaryOperator.values())
				.filter(type -> type.name.equals(name))
				.findFirst()
				.orElse(null);
	}

	public static String[] getValues() {
		return Stream.of(values()).map(t -> t.toString()).toArray(String[]::new);
	}

	public boolean equals(BinaryOperator... operators) {
		return Stream.of(operators).anyMatch(op -> op.equals(this));
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
