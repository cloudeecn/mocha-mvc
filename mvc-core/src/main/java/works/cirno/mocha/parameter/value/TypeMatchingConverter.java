package works.cirno.mocha.parameter.value;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;

public class TypeMatchingConverter implements ValueConverter {
	private static final Logger log = LoggerFactory.getLogger(TypeMatchingConverter.class);

	@Override
	public Object getValue(InvokeContext ctx, Class<?> type, ParameterSourcePool sourcePool, String key) {
		ParameterSource<?>[] sources;

		// Same type first
		sources = sourcePool.getParameterSourcesFor(type);
		for (ParameterSource<?> source : sources) {
			Object ret = source.getParameter(ctx, key);
			if (ret != null) {
				return ret;
			}
		}
		if (type.isArray()) {
			// Single type to array
			Class<?> componentType = type.getComponentType();
			sources = sourcePool.getParameterSourcesFor(componentType);
			for (ParameterSource<?> source : sources) {
				Object component = source.getParameter(ctx, key);
				if (component != null) {
					Object ret = Array.newInstance(componentType, 1);
					Array.set(ret, 0, component);
					return ret;
				}
			}
		} else {
			// Array to single type
			try {
				sources = sourcePool.getParameterSourcesFor(Class.forName(type.getName() + "[]"));
				for (ParameterSource<?> source : sources) {
					Object ret = source.getParameter(ctx, key);
					if (ret != null) {
						return Array.getLength(ret) == 0 ? null : Array.get(ret, 0);
					}
				}
			} catch (ClassNotFoundException e) {
				log.warn("Can't find class {}, skip [] converting", type.getName() + "[]", type.getName());
			}
		}
		return UNSUPPORTED_TYPE;
	}

}
