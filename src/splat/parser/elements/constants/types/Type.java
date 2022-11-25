package splat.parser.elements.constants.types;

import splat.parser.elements.constants.keyword.IKeyword;

public class Type implements IKeyword {
	public static final String INTEGER = "Integer";
	public static final String BOOLEAN = "Boolean";
	public static final String STRING = "String";
	private String type = null;

	public static String[] getValues() {
		return new String[] { INTEGER, BOOLEAN, STRING };
	}

	public Type(String type) {
		if (type.equals(INTEGER) || type.equals(BOOLEAN) || type.equals(STRING)) {
			this.type = type;
		}
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return type;
	}

	public boolean equalsTo(String type) {
		if (this instanceof RetType) {
			return ((RetType) this).getType().equals(type);
		}

		return this.type.equals(type);

	}

	public boolean equalsTo(Type type) {
		if (type instanceof RetType) {
			return this.type.equals(((RetType) type).getType());
		}

		if (this instanceof RetType) {
			return ((RetType) this).getType().equals(type.getType());
		}

		return this.type.equals(type.getType());

	}
}