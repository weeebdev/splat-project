package splat.parser.elements.constants.types;

import java.util.stream.Stream;

// Since enums cannot extend, I have to call methods manually
public class RetType extends Type {
	public static final String VOID = "void";
	private String type = null;

	public RetType(String type) {
		super(type);
		this.type = super.getType();
		if (type.equals(VOID)) {
			this.type = type;
		}
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}

	public static String[] getValues() {
		String[] values = Stream.concat(Stream.of(VOID), Stream.of(Type.getValues())).toArray(String[]::new);
		return values;
	}
}