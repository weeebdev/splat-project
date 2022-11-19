package splat.parser.elements.constants.types;

import java.util.stream.Stream;

import splat.parser.elements.constants.keyword.IKeyword;

// Since enums cannot extend, I have to call methods manually
public enum RetType implements IKeyword {

	VOID("void");

	private String name;

	private RetType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static RetType getEnum(String name) {
		IKeyword retType = Type.getEnum(name);
		if (retType != null) {
			retType = Stream.of(RetType.values())
					.filter(type -> type.name.equals(name))
					.findFirst()
					.orElse(null);
		}

		return (RetType) retType;
	}

	public static String[] getValues() {
		Type[] types = Type.values();
		RetType[] retTypes = RetType.values();
		return Stream.concat(Stream.of(types), Stream.of(retTypes)).map(t -> t.toString()).toArray(String[]::new);
	}

	public String toString() {
		return name;
	}
}