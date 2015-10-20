package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public class StringEnumConverter extends AbstractStringArrayConverter {

	@Override
	@SuppressWarnings("unchecked")
	protected Object convertOne(Class<?> type, InvokeContext ctx, Object from) {
		if (!type.isEnum()) {
			return NOT_CONVERTABLE;
		}
		if (from != null && from.getClass() != String.class) {
			return NOT_CONVERTABLE;
		}
		return Enum.valueOf(type.asSubclass(Enum.class), (String) from);
	}

}
