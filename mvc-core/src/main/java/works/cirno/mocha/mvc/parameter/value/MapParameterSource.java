package works.cirno.mocha.mvc.parameter.value;

import java.lang.reflect.Array;
import java.util.Map;

public class MapParameterSource implements ParameterSource {
	private final Map<String, ?> map;

	public MapParameterSource(Map<String, ?> map) {
		super();
		this.map = map;
	}

	public Object getParameter(String key) {
		return map.get(key);
	}

	public Object[] getParameters(String key) {
		Object value = map.get(key);
		if (value == null) {
			return null;
		} else {
			Class<?> clazz = value.getClass();
			if (clazz.isArray()) {
				return (Object[]) value;
			} else if (clazz.isPrimitive()) {
                throw new IllegalStateException("You can't get a primitive from a map");
			} else {
				Object[] ret = (Object[]) Array.newInstance(clazz, 1);
				ret[0] = value;
				return ret;
			}
		}
	}

}
