package works.cirno.mocha.mvc;

/**
 *
 */
public interface ConfigBuilder {
    String getPath();

    ConfigBuilder with(Class<?> controller, String methodName);

    ConfigBuilder method(String method);

    <T extends Throwable> ConfigBuilder exception(Class<T> exception, ExceptionHandler<T> handler);

    <T extends Throwable> ConfigBuilder exception(Class<T> exception, Result result);
}
