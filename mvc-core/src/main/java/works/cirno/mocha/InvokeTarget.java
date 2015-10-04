package works.cirno.mocha;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.parameter.name.Parameter;
import works.cirno.mocha.parameter.value.ParameterSourcePool;
import works.cirno.mocha.parameter.value.ValueConverter;
import works.cirno.mocha.parameter.value.ValueConverterUtil;
import works.cirno.mocha.result.ResultRenderer;

/**
 *
 */
public class InvokeTarget {
	private static final HashMap<Class<?>, Object> defaultPrimitives = new HashMap<Class<?>, Object>();

	static {
		defaultPrimitives.put(Boolean.TYPE, false);
		defaultPrimitives.put(Byte.TYPE, (byte) 0);
		defaultPrimitives.put(Character.TYPE, (char) 0);
		defaultPrimitives.put(Short.TYPE, (short) 0);
		defaultPrimitives.put(Integer.TYPE, 0);
		defaultPrimitives.put(Float.TYPE, 0f);
		defaultPrimitives.put(Long.TYPE, 0l);
		defaultPrimitives.put(Double.TYPE, 0d);
	}

	private final Logger log;

	// private final Invoker invoker;
	// private final Class<?> controllerClass;
	// private final String methodName;

	private Map<Class<?>, ResultRenderer> exceptionHandlers = new HashMap<>();
	private Set<String> groupNames;
	private List<ResultRenderer> resultRenderers;

	private Object controller;
	private Method method;
	private Parameter[] parameters;

	private List<ValueConverter> valueConverters;
	private ParameterSourcePool parameterSourcePool;

	private boolean raw;
	private File uploadTemp;

	public InvokeTarget(Dispatcher dispatcher, Object controller, String methodName) {
		Class<?> controllerClass = controller.getClass();
		this.log = LoggerFactory
				.getLogger(getClass().getName() + ".(" + controller.getClass().getName() + "." + methodName + ")");

		this.controller = controller;
		Method[] methods = controllerClass.getMethods();
		boolean resolved = false;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				if (resolved) {
					throw new IllegalArgumentException(
							"Duplicated method " + methodName + " in controller " + controllerClass.getName());
				} else {
					this.method = method;
					resolved = true;
				}
			}
		}
		if (this.method == null) {
			throw new IllegalArgumentException(
					"Can't find method " + methodName + " in controller " + controllerClass.getName());
		}
	}

	void setExceptionHandlers(Map<Class<?>, ResultRenderer> exceptionHandlers) {
		this.exceptionHandlers.clear();
		this.exceptionHandlers.putAll(exceptionHandlers);
	}

	void setParameters(Parameter[] parameters) {
		this.parameters = parameters.clone();
	}

	void setResultRenderers(List<ResultRenderer> resultRenderers) {
		this.resultRenderers = new ArrayList<>(resultRenderers);
	}

	void setGroupNames(Set<String> groupNames) {
		this.groupNames = groupNames;
	}

	void setRaw(boolean raw) {
		this.raw = raw;
	}

	void setUploadTemp(File uploadTemp) {
		this.uploadTemp = uploadTemp;
	}

	void setValueConverters(List<ValueConverter> valueConverters) {
		this.valueConverters = valueConverters;
	}

	void setParameterSourcePool(ParameterSourcePool parameterSourcePool) {
		this.parameterSourcePool = parameterSourcePool;
	}

	public Class<?> getControllerClass() {
		return controller.getClass();
	}

	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

	public Parameter parameterAt(int idx) {
		return parameters[idx];
	}

	public int parameterCount() {
		return parameters.length;
	}

	public List<ResultRenderer> getResultRenderers() {
		return resultRenderers;
	}

	public Logger log() {
		return log;
	}

	public Set<String> getGroupNames() {
		return groupNames;
	}

	public void invoke(InvokeContext context, HttpServletRequest req, HttpServletResponse resp) {

		int parametersCount = parameters.length;
		Object[] invokeParams = new Object[parametersCount];

		try {
			// bind parameter
			for (int i = 0; i < parametersCount; i++) {
				Parameter parameter = parameters[i];
				String name = parameter.getName();
				Class<?> type = parameter.getType();

				Object value = ValueConverterUtil.convert(context, valueConverters, parameterSourcePool, type, name);
				if (value == null && type.isPrimitive()) {
					value = defaultPrimitives.get(type);
				}
				invokeParams[i] = value;
			}
		} catch (Throwable t) {
			log.error("Exception occurred binding parameters {}{}", req.getRequestURI(),
					req.getQueryString() != null ? req.getQueryString() : "", t);
			handleException(context, req, resp, t);
		}
		try {
			Object result = method.invoke(controller, invokeParams);
			handleResult(context, req, resp, result);
		} catch (Throwable t) {
			log.error("Exception occurred processing request {}{}", req.getRequestURI(),
					req.getQueryString() != null ? req.getQueryString() : "", t);
			handleException(context, req, resp, t);
		}
	}

	public void handleResult(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Object resultObj) {
		if (resultObj != null && resultObj instanceof ResultRenderer) {
			ResultRenderer result = (ResultRenderer) resultObj;
			try {
				if (!result.renderResult(ctx, req, resp, null)) {
					handleException(ctx, req, resp,
							new IllegalStateException("Can't handle specified renderer: " + resultObj));
				}
			} catch (Exception e) {
				log.error("Exception occurred processing result of request {}{}", req.getRequestURI(),
						req.getQueryString() != null ? req.getQueryString() : "", e);
				handleException(ctx, req, resp, e);
			}
		} else {
			boolean handled = false;
			for (ResultRenderer renderer : resultRenderers) {
				if (renderer.renderResult(ctx, req, resp, resultObj)) {
					handled = true;
					break;
				}
			}
			if (!handled) {
				log.error("Exception occurred processing result of request {}{}, Can't handle specified result: {}",
						req.getRequestURI(), req.getQueryString() != null ? req.getQueryString() : "", resultObj);
				handleException(ctx, req, resp,
						new IllegalStateException("Can't handle specified result: " + resultObj));
			}
		}
	}

	public void handleException(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Throwable e) {
		Class<?> exceptionType = e.getClass();
		ResultRenderer renderer;
		do {
			renderer = exceptionHandlers.get(exceptionType);
			if (renderer != null) {
				break;
			}
		} while (Throwable.class.isAssignableFrom(exceptionType = exceptionType.getSuperclass()));

		if (renderer != null) {
			try {
				renderer.renderResult(ctx, req, resp, e);
			} catch (Throwable ex) {
				log.error("Exception occored handling exception: {}", e, ex);
			}
		} else {
			try {
				resp.sendError(500, "Server internal error");
			} catch (Throwable ex) {
				log.warn("Can't send error page to client due to IOException, exception is {}", e, ex);
			}
		}
	}
}
