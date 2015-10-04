package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public interface ParameterSource<T> {

	Class<T> supportsType();

	T getParameter(InvokeContext ctx, String key);
}
