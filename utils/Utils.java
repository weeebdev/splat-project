package utils;

import java.util.List;
import java.util.stream.Collectors;

// utils class for static helper methods
public class Utils {
	public static final String TAB = "   ";

	public static String formatArray(List list, String delimeter) {
		return (String) list.stream().map(Object::toString).collect(Collectors.joining(delimeter));
	}
}
