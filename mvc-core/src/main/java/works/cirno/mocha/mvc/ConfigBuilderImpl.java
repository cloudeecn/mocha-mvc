package works.cirno.mocha.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.result.ResultRenderer;

/**
 *
 */
class ConfigBuilderImpl implements ConfigBuilder {

	private String path;
	private String method;

	private MVCConfig config;

	ConfigBuilderImpl(String path, MVCConfig config) {
		this(path, null, config);
	}

	ConfigBuilderImpl(String path, String method, MVCConfig config) {
		this.path = path;
		this.method = method;
		this.config = new MVCConfig(config);
	}

	String getPath() {
		return path;
	}

	String getMethod() {
		return method;
	}

	Class<?> getController() {
		return config.getController();
	}

	String getMethodName() {
		return config.getMethodName();
	}

	ObjectFactory getObjectFactory() {
		return config.getObjectFactory();
	}

	ParameterAnalyzer getParameterAnalyzer() {
		return config.getParameterAnalyzer();
	}

	TreeMap<ComparableClassWrapper, ResultRenderer> getHandlers() {
		return config.getHandlers();
	}

	ArrayList<ResultRenderer> getResultRenderers() {
		return config.getResultRenderers();
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		return config.getPendingServletResultRendererConfig();
	}

	public ConfigBuilder with(Class<?> controller, String methodName) {
		return config.with(controller, methodName);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result) {
		return config.exception(exception, result);
	}

	public ConfigBuilder injectBy(ObjectFactory factory) {
		return config.injectBy(factory);
	}

	public ServletResultRendererConfig forward(String resultName) {
		return config.forward(resultName);
	}

	public ServletResultRendererConfig redirect(String resultName) {
		return config.redirect(resultName);
	}

	public ConfigBuilder renderer(ResultRenderer renderer) {
		return config.renderer(renderer);
	}

	public ConfigBuilder parameterNamedBy(ParameterAnalyzer pa) {
		return config.parameterNamedBy(pa);
	}

}
