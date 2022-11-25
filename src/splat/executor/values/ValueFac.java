package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.constants.types.Type;

public class ValueFac {
	public static Value createValue(Type type) {
		switch (type.getType()) {
			case "Integer":
				return new IntegerValue(type, 0);
			case "Boolean":
				return new BooleanValue(type, false);
			case "String":
				return new StringValue(type, "");
			default:
				return null;
		}
	}

	public static Value createValue(Type b, Object o) {
		Value val = createValue(b);
		val.setValue(o);
		return val;
	}

	public static Value createValue(String type, String value) {
		Type t = new Type(type);
		switch (t.getType()) {
			case "Integer":
				return new IntegerValue(new Type(type), Integer.parseInt(value));
			case "Boolean":
				return new BooleanValue(new Type(type), Boolean.parseBoolean(value));
			case "String":
				return new StringValue(new Type(type), value);
			default:
				return null;
		}
	}

	public static Value createValue(String type, Object o) {
		Type t = new Type(type);
		switch (t.getType()) {
			case "Integer":
				return new IntegerValue(new Type(type), (Integer) o);
			case "Boolean":
				return new BooleanValue(new Type(type), (Boolean) o);
			case "String":
				return new StringValue(new Type(type), (String) o);
			default:
				return null;
		}
	}
}
