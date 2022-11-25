package splat.executor;

import splat.parser.elements.constants.types.Type;

public abstract class Value {

	private Type type;
	private Object value;

	public Value(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public abstract String toString();
}
