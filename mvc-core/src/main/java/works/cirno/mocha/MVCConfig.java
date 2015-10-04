package works.cirno.mocha;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import works.cirno.mocha.parameter.value.NamedGroupParameterSource;
import works.cirno.mocha.parameter.value.ParameterSource;
import works.cirno.mocha.parameter.value.RequestArrayParameterSource;
import works.cirno.mocha.parameter.value.RequestRawSource;
import works.cirno.mocha.parameter.value.RequestSingleParameterSource;
import works.cirno.mocha.parameter.value.ResponseRawSource;
import works.cirno.mocha.parameter.value.SessionParameterSource;
import works.cirno.mocha.result.ResultRenderer;
import works.cirno.mocha.result.ResultType;

class MVCConfig implements ConfigBuilder {
	private static final List<TypeOrInstance<ParameterSource<?>>> DEFAULT_PARAMETER_SOURCES = Arrays.asList(//
			new TypeOrInstance<ParameterSource<?>>(RequestRawSource.class), //
			new TypeOrInstance<ParameterSource<?>>(ResponseRawSource.class), //
			new TypeOrInstance<ParameterSource<?>>(SessionParameterSource.class), //
			new TypeOrInstance<ParameterSource<?>>(NamedGroupParameterSource.class), //
			new TypeOrInstance<ParameterSource<?>>(RequestArrayParameterSource.class), //
			new TypeOrInstance<ParameterSource<?>>(RequestSingleParameterSource.class) //
	);

	private MVCConfig parent;

	private TypeOrInstance<?> controller;
	private String methodName;

	private File uploadTemp;
	private boolean raw;
	private Map<Class<?>, TypeOrInstance<? extends ResultRenderer>> handlers;
	private HashMap<Class<?>, TypeOrInstance<? extends ResultRenderer>> handlersCustom = new HashMap<>();
	private List<TypeOrInstance<? extends ResultRenderer>> resultRenderers;
	private List<TypeOrInstance<? extends ResultRenderer>> resultRenderersAppend = new ArrayList<>();
	private List<TypeOrInstance<? extends ResultRenderer>> resultRenderersPrepend = new LinkedList<>();

	private List<TypeOrInstance<? extends ParameterSource<?>>> parameterSources;
	private List<TypeOrInstance<? extends ParameterSource<?>>> parameterSourcesAppend = new ArrayList<>();
	private List<TypeOrInstance<? extends ParameterSource<?>>> parameterSourcesPrepend = new LinkedList<>();
	private HashMap<String, ServletResultRendererConfigImpl> pendingServletResultRendererConfig = new HashMap<>();

	public MVCConfig() {
	}

	public MVCConfig(MVCConfig parent) {
		this.parent = parent;
	}

	MVCConfig getParent() {
		return parent;
	}

	TypeOrInstance<?> getController() {
		return controller == null ? parent == null ? null : parent.getController() : controller;
	}

	String getMethodName() {
		return methodName == null ? parent == null ? null : parent.getMethodName() : methodName;
	}

	Map<Class<?>, TypeOrInstance<? extends ResultRenderer>> getHandlers() {
		if (handlers == null) {
			if (handlersCustom.size() == 0) {
				handlers = parent == null ? Collections.<Class<?>, TypeOrInstance<? extends ResultRenderer>> emptyMap()
						: parent.getHandlers();
			} else {
				handlers = new HashMap<>();
				if (parent != null) {
					handlers.putAll(parent.getHandlers());
				} else {
					handlers.putAll(handlersCustom);
				}
			}
		}
		return handlers;
	}

	List<TypeOrInstance<? extends ResultRenderer>> getResultRenderers() {
		if (resultRenderers == null) {
			if (resultRenderersAppend.size() + resultRenderersPrepend.size() == 0) {
				resultRenderers = parent == null ? Collections.<TypeOrInstance<? extends ResultRenderer>> emptyList()
						: parent.getResultRenderers();
			} else {
				resultRenderers = new ArrayList<>();
				resultRenderers.addAll(resultRenderersPrepend);
				if (parent != null) {
					resultRenderers.addAll(parent.getResultRenderers());
				}
				resultRenderers.addAll(resultRenderersAppend);
			}
		}
		return resultRenderers;
	}

	List<TypeOrInstance<? extends ParameterSource<?>>> getParameterSources() {
		if (parameterSources == null) {
			if (parameterSourcesAppend.size() + parameterSourcesPrepend.size() == 0) {
				parameterSources = parent == null
						? Collections.<TypeOrInstance<? extends ParameterSource<?>>> emptyList()
						: parent.getParameterSources();
			} else {
				parameterSources = new ArrayList<>();
				parameterSources.addAll(parameterSourcesPrepend);
				if (parent != null) {
					parameterSources.addAll(parent.getParameterSources());
				}
				parameterSources.addAll(parameterSourcesAppend);
			}
		}
		return parameterSources;
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		HashMap<String, ServletResultRendererConfigImpl> psrrConfig = new HashMap<>();
		if (parent != null) {
			psrrConfig.putAll(parent.getPendingServletResultRendererConfig());
		}
		psrrConfig.putAll(pendingServletResultRendererConfig);
		return psrrConfig;
	}

	boolean isRaw() {
		return raw ? true : (parent == null ? false : parent.isRaw());
	}

	File getUploadTemp() {
		return uploadTemp == null
				? parent == null ? new File(System.getProperty("java.io.tmpdir")) : parent.getUploadTemp() : uploadTemp;
	}

	@Override
	public ConfigBuilder with(Class<?> controller, String methodName) {
		with(controller);
		withMethod(methodName);
		return this;
	}

	@Override
	public ConfigBuilder with(String controllerName, String methodName) {
		with(controllerName);
		withMethod(methodName);
		return this;
	}

	@Override
	public ConfigBuilder with(String controllerName) {
		this.controller = new TypeOrInstance<>(controllerName, Object.class);
		return this;
	}

	@Override
	public ConfigBuilder with(Class<?> controller) {
		this.controller = new TypeOrInstance<>(controller);
		return this;
	}

	@Override
	public ConfigBuilder withMethod(String methodName) {
		this.methodName = methodName;
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result) {
		this.handlers.put(exception, new TypeOrInstance<>(result));
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception,
			Class<? extends ResultRenderer> resultType) {
		this.handlers.put(exception, new TypeOrInstance<>(resultType));
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, String resultName) {
		this.handlers.put(exception, new TypeOrInstance<>(resultName, ResultRenderer.class));
		return this;
	}

	@Override
	public ServletResultRendererConfig forward(String resultName) {
		ServletResultRendererConfigImpl srrConfig = new ServletResultRendererConfigImpl(this, ResultType.forward);
		pendingServletResultRendererConfig.put(resultName, srrConfig);
		return srrConfig;
	}

	@Override
	public ServletResultRendererConfig redirect(String resultName) {
		ServletResultRendererConfigImpl srrConfig = new ServletResultRendererConfigImpl(this, ResultType.redirect);
		pendingServletResultRendererConfig.put(resultName, srrConfig);
		return srrConfig;
	}

	@Override
	public ConfigBuilder prependResultRenderer(ResultRenderer renderer) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder prependResultRenderer(Class<? extends ResultRenderer> renderer) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder prependResultRenderer(String rendererName) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(rendererName, ResultRenderer.class));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(ResultRenderer renderer) {
		resultRenderersAppend.add(new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(Class<? extends ResultRenderer> renderer) {
		resultRenderersAppend.add(new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(String rendererName) {
		resultRenderersAppend.add(new TypeOrInstance<>(rendererName, ResultRenderer.class));
		return this;
	}

	@Override
	public ConfigBuilder raw() {
		this.raw = true;
		return this;
	}

	@Override
	public ConfigBuilder uploadTemp(File tempDirectory) {
		this.uploadTemp = tempDirectory;
		return this;
	}
}
