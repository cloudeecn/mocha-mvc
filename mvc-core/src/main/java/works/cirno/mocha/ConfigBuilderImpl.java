package works.cirno.mocha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import works.cirno.mocha.result.ResultRenderer;

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

	Class<?> getControllerClass() {
		return config.getControllerClass();
	}
	
	String getControllerName(){
		return config.getControllerName();
	}

	String getMethodName() {
		return config.getMethodName();
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

	@Override
	public ConfigBuilder with(Class<?> controller, String methodName) {
		return config.with(controller, methodName);
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result) {
		return config.exception(exception, result);
	}

	@Override
	public ServletResultRendererConfig forward(String resultName) {
		return config.forward(resultName);
	}

	@Override
	public ServletResultRendererConfig redirect(String resultName) {
		return config.redirect(resultName);
	}

	@Override
	public ConfigBuilder renderer(ResultRenderer renderer) {
		return config.renderer(renderer);
	}

	@Override
	public ConfigBuilder rawEntity() {
		return config.rawEntity();
	}

	@Override
	public ConfigBuilder with(String controllerName, String methodName) {
		return config.with(controllerName, methodName);
	}

	@Override
	public ConfigBuilder with(String controllerName) {
		return config.with(controllerName);
	}

	@Override
	public ConfigBuilder with(Class<?> controller) {
		return config.with(controller);
	}

	@Override
	public ConfigBuilder withMethod(String methodName) {
		return config.withMethod(methodName);
	}

}
