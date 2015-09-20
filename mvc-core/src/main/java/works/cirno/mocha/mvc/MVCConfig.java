package works.cirno.mocha.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import works.cirno.mocha.mvc.factory.BasicObjectFactory;
import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.AnnotationParameterAnalyzer;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.result.ResultRenderer;
import works.cirno.mocha.mvc.result.ResultType;

class MVCConfig implements ConfigBuilder {
	private MVCConfig parent;

	private Class<?> controller;
	private String methodName;

	private ObjectFactory objectFactory;
	private ParameterAnalyzer parameterAnalyzer;
	private TreeMap<ComparableClassWrapper, ResultRenderer> handlers = new TreeMap<>();
	private ArrayList<ResultRenderer> resultRenderers = new ArrayList<>();
	private HashMap<String, ServletResultRendererConfigImpl> pendingServletResultRendererConfig = new HashMap<>();

	public MVCConfig() {
	}

	public MVCConfig(MVCConfig parent) {
		this.parent = parent;
	}

	MVCConfig getParent() {
		return parent;
	}

	Class<?> getController() {
		return controller == null ? parent == null ? null : parent.getController() : controller;
	}

	String getMethodName() {
		return methodName == null ? parent == null ? null : parent.getMethodName() : methodName;
	}

	ObjectFactory getObjectFactory() {
		return objectFactory == null ? parent == null ? new BasicObjectFactory() : parent.getObjectFactory()
				: objectFactory;
	}

	ParameterAnalyzer getParameterAnalyzer() {
		return parameterAnalyzer == null
				? parent == null ? new AnnotationParameterAnalyzer() : parent.getParameterAnalyzer()
				: parameterAnalyzer;
	}

	TreeMap<ComparableClassWrapper, ResultRenderer> getHandlers() {
		TreeMap<ComparableClassWrapper, ResultRenderer> handlers = new TreeMap<>();
		if (parent != null) {
			handlers.putAll(parent.getHandlers());
		}
		handlers.putAll(this.handlers);
		return handlers;
	}

	ArrayList<ResultRenderer> getResultRenderers() {
		ArrayList<ResultRenderer> renderers = new ArrayList<>();
		renderers.addAll(this.resultRenderers);
		if (parent != null) {
			renderers.addAll(parent.getResultRenderers());
		}
		return renderers;
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		HashMap<String, ServletResultRendererConfigImpl> psrrConfig = new HashMap<>();
		if (parent != null) {
			psrrConfig.putAll(parent.getPendingServletResultRendererConfig());
		}
		psrrConfig.putAll(pendingServletResultRendererConfig);
		return psrrConfig;
	}

	@Override
	public ConfigBuilder with(Class<?> controller, String methodName) {
		this.controller = controller;
		this.methodName = methodName;
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result) {
		this.handlers.put(new ComparableClassWrapper(exception), result);
		return this;
	}

	@Override
	public ConfigBuilder injectBy(ObjectFactory factory) {
		this.objectFactory = factory;
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
	public ConfigBuilder renderer(ResultRenderer renderer) {
		resultRenderers.add(renderer);
		return this;
	}

	@Override
	public ConfigBuilder parameterNamedBy(ParameterAnalyzer parameterAnalyzer) {
		this.parameterAnalyzer = parameterAnalyzer;
		return this;
	}

}
