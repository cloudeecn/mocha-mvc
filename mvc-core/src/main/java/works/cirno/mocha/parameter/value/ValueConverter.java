package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public interface ValueConverter {

	public final static Object UNSUPPORTED_TYPE = new Object();

	Object getValue(InvokeContext ctx, Class<?> type, ParameterSourcePool sources, String key);
}
