package works.cirno.mocha.parameter.value;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

public class PartConverter implements ValueConverter {

	@Override
	public Object getValue(Class<?> type, ParameterSource source, String key) {
		if (InputStream.class.equals(type)) {
			Object part = source.getParameter(key);
			if (part != null && part instanceof Part) {
				try {
					return ((Part) part).getInputStream();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				return UNSUPPORTED_VALUE;
			}
		} else if (Part.class.equals(type)) {
			Object part = source.getParameter(key);
			if (part != null && part instanceof Part) {
				return part;
			} else {
				return UNSUPPORTED_VALUE;
			}
		} else {
			return UNSUPPORTED_TYPE;
		}
	}

}
