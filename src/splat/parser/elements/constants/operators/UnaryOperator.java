package splat.parser.elements.constants.operators;

import java.util.stream.Stream;

import splat.parser.elements.constants.keyword.IKeyword;

public enum UnaryOperator implements IKeyword {
	MINUS("-"), NOT("not");

	private String name;

	private UnaryOperator(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static UnaryOperator getEnum(String name) {
		return Stream.of(UnaryOperator.values())
				.filter(type -> type.name.equals(name))
				.findFirst()
				.orElse(null);
	}

	public static String[] getValues() {
		return Stream.of(values()).map(t -> t.toString()).toArray(String[]::new);
	}

	public String toString() {
		return name;
	}
}
