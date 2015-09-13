package works.cirno.mocha.mvc.parameter.value;

public interface ValueConverter {
	static final Object UNSUPPORTED_TYPE = new Object();
	static final Object UNSUPPORTED_VALUE = new Object();

	Object getValue(Class<?> type, ParameterSource source, String key);
}
