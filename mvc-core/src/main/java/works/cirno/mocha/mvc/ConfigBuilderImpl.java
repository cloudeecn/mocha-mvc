package works.cirno.mocha.mvc;

import java.util.TreeMap;

/**
 *
 */
class ConfigBuilderImpl implements ConfigBuilder {

    String path;
    String method;
    Class<?> controller;
    String methodName;
    TreeMap<ComparableClassWrapper, ExceptionHandler<?>> handlers = new TreeMap<>();

    ConfigBuilderImpl(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ConfigBuilder with(Class<?> controller, String methodName) {
        this.controller = controller;
        this.methodName = methodName;
        return this;
    }

    @Override
    public ConfigBuilder method(String method) {
        this.method = method;
        return this;
    }

    public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ExceptionHandler<T> handler) {
        ExceptionHandler<?> old = handlers.put(new ComparableClassWrapper(exception), handler);
        if (old != null) {
            throw new IllegalArgumentException("Exception " + old + " already handled");
        }
        return this;
    }

    public <T extends Throwable> ConfigBuilder exception(Class<T> exception, Result result) {
        return exception(exception, new ResultHandler<T>(result));
    }

}
