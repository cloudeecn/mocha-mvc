package works.cirno.mocha.mvc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.mvc.parameter.name.Parameter;
import works.cirno.mocha.mvc.parameter.value.NamedGroupParameterSource;
import works.cirno.mocha.mvc.parameter.value.ParameterSource;
import works.cirno.mocha.mvc.parameter.value.RequestParameterSource;
import works.cirno.mocha.mvc.parameter.value.ValueConverter;
import works.cirno.mocha.mvc.parameter.value.ValueConverterUtil;
import works.cirno.mocha.mvc.result.ResultRenderer;

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
	private final Class<?> controllerClass;
	// private final String methodName;

	private TreeMap<ComparableClassWrapper, ResultRenderer> exceptionHandlers = new TreeMap<>();
	private Set<String> groupNames;
	private List<ResultRenderer> resultRenderers;

	private Object controller;
	private Method method;
	private Parameter[] parameters;

	public InvokeTarget(Dispatcher dispatcher, Class<?> controllerClass, String methodName, Object controller) {
		// this.invoker = invoker;
		this.log = LoggerFactory
				.getLogger(getClass().getName() + ".(" + controllerClass.getName() + "." + methodName + ")");
		this.controllerClass = controllerClass;
		// this.methodName = methodName;

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

		parameters = new Parameter[this.method.getParameterTypes().length];
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

	public void setGroupNames(Set<String> groupNames) {
		this.groupNames = groupNames;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
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
		parameterSources.add(new RequestParameterSource(req));

		int parametersCount = parameters.length;
		Object[] invokeParams = new Object[parametersCount];

		try {
			// bind parameter
			for (int i = 0; i < parametersCount; i++) {
				Parameter parameter = parameters[i];
				Object value = ValueConverter.UNSUPPORTED_TYPE;
				Class<?> type = parameter.getType();

				if ((type.isPrimitive() || type.isArray()) && parameter != null) {
					String name = parameter.getName();
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
				} else if (type.isAssignableFrom(HttpServletRequest.class)) {
					value = req;
				} else if (type.isAssignableFrom(HttpServletResponse.class)) {
					value = resp;
				} else if (type.isAssignableFrom(ServletOutputStream.class)) {
					value = resp.getOutputStream();
				} else if (type.isAssignableFrom(ServletInputStream.class)) {
					value = req.getInputStream();
				} else if (parameter != null) {
					String name = parameter.getName();
					if (log.isDebugEnabled()) {
						log.debug("CONV {}({})", name, "OBJ");
					}
					value = ValueConverterUtil.convert(ValueConverterUtil.DEFAULT_VALUE_CONVERTERS, parameterSources,
							type, name);
					if (value == ValueConverter.UNSUPPORTED_VALUE) {
						if (log.isDebugEnabled()) {
							log.debug("CONV {}({})", name, "BEN");
						}
						value = ValueConverterUtil.convertBean(ValueConverterUtil.DEFAULT_VALUE_CONVERTERS,
								parameterSources, type, name);
					}
					if (value == ValueConverter.UNSUPPORTED_TYPE || value == ValueConverter.UNSUPPORTED_VALUE) {
						value = null;
					}
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
				if (!result.renderResult(ctx, req, resp, result)) {
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
