package works.cirno.mocha;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.parameter.name.Parameter;
import works.cirno.mocha.parameter.value.NamedGroupParameterSource;
import works.cirno.mocha.parameter.value.ParameterSource;
import works.cirno.mocha.parameter.value.RequestParameterSource;
import works.cirno.mocha.parameter.value.RequestPartSource;
import works.cirno.mocha.parameter.value.RequestRawSource;
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

	private TreeMap<ComparableClassWrapper, ResultRenderer> exceptionHandlers = new TreeMap<>();
	private Set<String> groupNames;
	private List<ResultRenderer> resultRenderers;

	private Object controller;
	private Method method;
	private Parameter[] parameters;
	private boolean rawEntity;

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

	void setExceptionHandlers(TreeMap<ComparableClassWrapper, ResultRenderer> exceptionHandlers) {
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

	void setRawEntity(boolean rawEntity) {
		this.rawEntity = rawEntity;
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

	public void invoke(InvokeContext context, HttpServletRequest req, HttpServletResponse resp) {

		ArrayList<ParameterSource> parameterSources = new ArrayList<>(4);
		if (context.getUriMatcher() != null) {
			parameterSources.add(new NamedGroupParameterSource(context.getUriMatcher(), groupNames));
		}
		if (rawEntity) {
			parameterSources.add(new RequestRawSource(req));
		} else {
			if (RequestPartSource.isMultipart(req)) {
				parameterSources.add(new RequestPartSource(req));
			}
			parameterSources.add(new RequestParameterSource(req));
		}

		int parametersCount = parameters.length;
		Object[] invokeParams = new Object[parametersCount];

		try {
			// bind parameter
			for (int i = 0; i < parametersCount; i++) {
				Parameter parameter = parameters[i];
				String name = parameter.getName();
				Class<?> type = parameter.getType();
				Object value = ValueConverter.UNSUPPORTED_TYPE;
				if ((type.isPrimitive() || type.isArray()) && name != null) {
					if (log.isDebugEnabled()) {
						log.debug("CONV {}({})", name, type.isPrimitive() ? "PRI" : "ARR");
					}
					value = ValueConverterUtil.convert(ValueConverterUtil.DEFAULT_VALUE_CONVERTERS, parameterSources,
							type, name);
					if (value == ValueConverter.UNSUPPORTED_TYPE || value == ValueConverter.UNSUPPORTED_VALUE) {
						if (type.isPrimitive()) {
							value = defaultPrimitives.get(type);
						} else {
							value = null;
						}
					}
				} else if (type.isAssignableFrom(HttpServletRequest.class)
						&& ServletRequest.class.isAssignableFrom(type)) {
					value = req;
				} else if (type.isAssignableFrom(HttpServletResponse.class)
						&& ServletResponse.class.isAssignableFrom(type)) {
					value = resp;
				} else if (type.isAssignableFrom(ServletOutputStream.class)
						&& OutputStream.class.isAssignableFrom(type)) {
					value = resp.getOutputStream();
				} else if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug("CONV {}({})", name, "OBJ");
					}
					value = ValueConverterUtil.convert(ValueConverterUtil.DEFAULT_VALUE_CONVERTERS, parameterSources,
							type, name);
					if ((value == ValueConverter.UNSUPPORTED_VALUE || value == ValueConverter.UNSUPPORTED_TYPE)
							&& !type.isArray() && !type.isPrimitive() && !type.isInterface()) {
						if (log.isDebugEnabled()) {
							log.debug("CONV {}({})", name, "BEN");
						}
						value = ValueConverterUtil.convertBean(ValueConverterUtil.DEFAULT_VALUE_CONVERTERS,
								parameterSources, type, name);
					}
					if (value == ValueConverter.UNSUPPORTED_TYPE || value == ValueConverter.UNSUPPORTED_VALUE) {
						value = null;
					}
				} else {
					value = null;
				}
				invokeParams[i] = value;
			}
		} catch (IOException e) {
			log.error("Exception occurred binding parameters {}{}", req.getRequestURI(),
					req.getQueryString() != null ? req.getQueryString() : "", e);
			handleException(context, req, resp, e);
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
		Class<? extends Throwable> exceptionType = e.getClass();
		Entry<ComparableClassWrapper, ResultRenderer> entry = exceptionHandlers
				.ceilingEntry(new ComparableClassWrapper(exceptionType));

		if (entry != null && entry.getKey().getContent().isAssignableFrom(exceptionType)) {
			try {
				entry.getValue().renderResult(ctx, req, resp, e);
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