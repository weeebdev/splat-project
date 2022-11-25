package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.constants.types.Type;

public class IntegerValue extends Value {
	private Integer value;

	public IntegerValue(Type type, Integer value) {
		super(type, value);
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s", value);
	}

}
