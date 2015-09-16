package works.cirno.mocha.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import works.cirno.mocha.mvc.result.CodeResultRenderer;
import works.cirno.mocha.mvc.result.ResultRenderer;
import works.cirno.mocha.mvc.result.ResultType;
import works.cirno.mocha.mvc.result.ServletResultRenderer;

/**
 *
 */
class ConfigBuilderImpl implements ConfigBuilder {

	String path;
	String method;
	Class<?> controller;
	String methodName;
	TreeMap<ComparableClassWrapper, ResultRenderer> handlers = new TreeMap<>();
	private ServletResultRenderer servletResultRenderer = new ServletResultRenderer();
	List<ResultRenderer> resultRenderers = new ArrayList<>(
			Arrays.asList(new CodeResultRenderer(), servletResultRenderer));
	HashMap<String, ServletResultRendererConfigImpl> pendingServletResultRendererConfig = new HashMap<>();

	ConfigBuilderImpl(String path) {
		this.path = path;
	}

	void finishConfigure() {
		for (Entry<String, ServletResultRendererConfigImpl> entry : pendingServletResultRendererConfig.entrySet()) {
			servletResultRenderer.addResult(entry.getKey(), entry.getValue().getResultType(),
					entry.getValue().getPath());
		}
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

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer handler) {
		ResultRenderer old = handlers.put(new ComparableClassWrapper(exception), handler);
		if (old != null) {
			throw new IllegalArgumentException("Exception " + old + " already handled");
		}
		return this;
	}

	@Override
	public ServletResultRendererConfig forward(String resultName) {
		ServletResultRendererConfigImpl conf = new ServletResultRendererConfigImpl(this, ResultType.forward);
		pendingServletResultRendererConfig.put(resultName, conf);
		return conf;
	}

	@Override
	public ServletResultRendererConfig redirect(String resultName) {
		ServletResultRendererConfigImpl conf = new ServletResultRendererConfigImpl(this, ResultType.redirect);
		pendingServletResultRendererConfig.put(resultName, conf);
		return conf;
	}

}
