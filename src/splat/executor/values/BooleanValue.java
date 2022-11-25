package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.constants.types.Type;

public class BooleanValue extends Value {
	private Boolean value;

	public BooleanValue(Type type, Boolean value) {
		super(type, value);
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s", value);
	}

}
