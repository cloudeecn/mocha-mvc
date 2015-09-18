package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.result.ResultRenderer;

/**
 *
 */
public interface ConfigBuilder {
	ConfigBuilder with(Class<?> controller, String methodName);

	<T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result);

	ConfigBuilder injectBy(ObjectFactory factory);

	ServletResultRendererConfig forward(String resultName);

	ServletResultRendererConfig redirect(String resultName);

	ConfigBuilder renderer(ResultRenderer renderer);

	ConfigBuilder parameterNamedBy(ParameterAnalyzer pa);
}
