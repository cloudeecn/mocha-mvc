package works.cirno.mocha;

import java.util.concurrent.ConcurrentHashMap;

/**
 * BasicObjectFactory will treat all beans as singleton,
 */
public class BasicMVCFactory implements MVCFactory {

	private final ConcurrentHashMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

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

	@Override
	public <T> T getControllerInstance(Class<T> clazz) {
		return getInstance(clazz);
	}

	@Override
	public <T extends MVCConfigurator> T getMVCConfiguratorInstance(Class<T> clazz) {
		return getInstance(clazz);
	}

	@Override
	public Object getControllerInstance(String name) {
		try {
			return getInstance(Class.forName(name));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
