package splat.parser.elements.constants.types;

import java.util.stream.Stream;

import splat.parser.elements.constants.keyword.IKeyword;

public enum Type implements IKeyword {
	INTEGER("Integer"),
	BOOLEAN("Boolean"),
	STRING("String");

	private String name;

	private Type(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Type getEnum(String name) {
		return Stream.of(Type.values())
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