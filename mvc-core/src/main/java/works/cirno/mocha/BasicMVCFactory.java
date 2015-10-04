package works.cirno.mocha;

import java.util.concurrent.ConcurrentHashMap;

/**
 * BasicObjectFactory will treat all beans as singleton,
 */
public class BasicMVCFactory implements ObjectFactory {

	private final ConcurrentHashMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

	@Override
	public <T> T getInstance(TypeOrInstance<T> param) {
		switch (param.getInjectBy()) {
		case TYPE:
			return getInstance(param.getType());
		case INSTANCE:
			return param.getInstance();
		default:
			throw new UnsupportedOperationException("Can't inject by " + param.getInjectBy());
		}

	}

	private <T> T getInstance(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T result = (T) instances.get(clazz);
		if (result == null) {
			try {
				result = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			instances.put(clazz, result);
		}
		return result;
	}

}
