package works.cirno.mocha;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.cirno.mocha.parameter.value.ParameterSource;
import works.cirno.mocha.result.Renderer;

/**
 *
 */
class ConfigBuilderImpl implements ConfigBuilder {

	private String path;
	private String method;

	private MVCConfig config;

	ConfigBuilderImpl(String path, MVCConfig parent) {
		this(path, null, parent);
	}

	ConfigBuilderImpl(String path, String method, MVCConfig parent) {
		this.path = path;
		this.method = method;
		this.config = new MVCConfig(parent);
	}

	String getPath() {
		return path;
	}

	String getMethod() {
		return method;
	}

	TypeOrInstance<?> getController() {
		return config.getController();
	}

	String getMethodName() {
		return config.getMethodName();
	}

	Map<Class<?>, TypeOrInstance<? extends Renderer>> getExceptionHandlers() {
		return config.getHandlers();
	}

	List<TypeOrInstance<? extends Renderer>> getResultRenderers() {
		return config.getResultRenderers();
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		return config.getPendingServletResultRendererConfig();
	}

	boolean isRaw() {
		return config.isRaw();
	}

	File getUploadTemp() {
		return config.getUploadTemp();
	}

	public ConfigBuilder with(Class<?> controller, String methodName) {
		return config.with(controller, methodName);
	}

	public ConfigBuilder with(String controllerName, String methodName) {
		return config.with(controllerName, methodName);
	}

	public ConfigBuilder with(String controllerName) {
		return config.with(controllerName);
	}

	public ConfigBuilder with(Class<?> controller) {
		return config.with(controller);
	}

	public ConfigBuilder withMethod(String methodName) {
		return config.withMethod(methodName);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, Renderer result) {
		return config.exception(exception, result);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception,
			Class<? extends Renderer> resultType) {
		return config.exception(exception, resultType);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, String resultName) {
		return config.exception(exception, resultName);
	}

	public ServletResultRendererConfig forward(String resultName) {
		return config.forward(resultName);
	}

	public ServletResultRendererConfig redirect(String resultName) {
		return config.redirect(resultName);
	}

	public ConfigBuilder prependResultRenderer(Renderer renderer) {
		return config.prependResultRenderer(renderer);
	}

	public ConfigBuilder prependResultRenderer(Class<? extends Renderer> renderer) {
		return config.prependResultRenderer(renderer);
	}

	public ConfigBuilder prependResultRenderer(String rendererName) {
		return config.prependResultRenderer(rendererName);
	}

	public ConfigBuilder appendResultRenderer(Renderer renderer) {
		return config.appendResultRenderer(renderer);
	}

	public ConfigBuilder appendResultRenderer(Class<? extends Renderer> renderer) {
		return config.appendResultRenderer(renderer);
	}

	public ConfigBuilder appendResultRenderer(String rendererName) {
		return config.appendResultRenderer(rendererName);
	}

	public ConfigBuilder raw() {
		return config.raw();
	}

	public ConfigBuilder uploadTemp(File tempDirectory) {
		return config.uploadTemp(tempDirectory);
	}

}
