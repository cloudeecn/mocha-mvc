package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.result.ResultRenderer;

/**
 *
 */
public interface ConfigBuilder {
	String getPath();

	ConfigBuilder with(Class<?> controller, String methodName);

	ConfigBuilder method(String method);

	<T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result);

	ServletResultRendererConfig forward(String resultName);

	ServletResultRendererConfig redirect(String resultName);

}
