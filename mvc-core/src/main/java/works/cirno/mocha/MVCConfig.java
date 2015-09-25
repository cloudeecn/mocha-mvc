package works.cirno.mocha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import works.cirno.mocha.result.ResultRenderer;
import works.cirno.mocha.result.ResultType;

class MVCConfig implements ConfigBuilder {
	private MVCConfig parent;

	private Class<?> controller;
	private String controllerName;
	private String methodName;

	private boolean rawEntity;
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

	Class<?> getControllerClass() {
		return controller == null ? (parent == null || controllerName != null) ? null : parent.getControllerClass()
				: controller;
	}

	String getControllerName() {
		return controllerName == null ? (parent == null || controller != null) ? null : parent.getControllerName()
				: controllerName;
	}

	String getMethodName() {
		return methodName == null ? parent == null ? null : parent.getMethodName() : methodName;
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

	boolean getRawEntity() {
		return rawEntity ? true : (parent == null ? false : parent.getRawEntity());
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
		this.controllerName = controllerName;
		this.controller = null;
		return this;
	}

	@Override
	public ConfigBuilder with(Class<?> controller) {
		this.controller = controller;
		this.controllerName = null;
		return this;
	}

	@Override
	public ConfigBuilder withMethod(String methodName) {
		this.methodName = methodName;
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result) {
		this.handlers.put(new ComparableClassWrapper(exception), result);
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
	public ConfigBuilder rawEntity() {
		this.rawEntity = true;
		return this;
	}
}
