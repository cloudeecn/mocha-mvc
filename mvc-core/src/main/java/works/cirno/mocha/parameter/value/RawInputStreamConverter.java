package works.cirno.mocha.parameter.value;

import java.io.InputStream;

public final class RawInputStreamConverter implements ValueConverter {

	@Override
	public Object getValue(Class<?> type, ParameterSource source, String key) {
		if (InputStream.class.equals(type)) {
			if (source instanceof RequestRawSource) {
				return source.getParameter(key);
			} else {
				return UNSUPPORTED_VALUE;
			}
		} else {
			return UNSUPPORTED_TYPE;
		}
	}

}
