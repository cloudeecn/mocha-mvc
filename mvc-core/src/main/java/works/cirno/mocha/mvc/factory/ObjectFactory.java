package works.cirno.mocha.mvc.factory;

/**
 *
 */
public interface ObjectFactory {
    <T> T getInstance(Class<T> clazz);

    <T> T getInstance(Class<T> clazz, String name);

    void inject(Object o);
}
