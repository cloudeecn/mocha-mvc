package works.cirno.mocha.mvc.factory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * BasicObjectFactory will treat all beans as singleton,
 */
public class BasicObjectFactory implements ObjectFactory {

    private final ConcurrentHashMap<BasicName, Object> instances = new ConcurrentHashMap<>();

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz, String name) {
        BasicName key = new BasicName(name, clazz);
        T result = (T) instances.get(key);
        if (result == null) {
            try {
                result = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            instances.put(key, result);
        }
        return result;
    }

    @Override
    public void inject(Object o) {

    }
}
