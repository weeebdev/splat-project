package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.constants.types.Type;

public class StringValue extends Value {
	private String value;

	public StringValue(Type type, String value) {
		super(type, value);
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s", value);
	}

}
