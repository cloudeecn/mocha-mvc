/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2015 Cloudee Huang ( https://github.com/cloudeecn / cloudeecn@gmail.com )
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.parameter.name.Parameter;
import works.cirno.mocha.parameter.value.ParameterSource;
import works.cirno.mocha.result.Renderer;

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
	private final Logger perfLog = LoggerFactory.getLogger("perf." + InvokeTarget.class.getName());

	// private final Invoker invoker;
	// private final Class<?> controllerClass;
	// private final String methodName;

	private Map<Class<?>, Renderer> exceptionHandlers = new HashMap<>();
	private Set<String> groupNames;
	private List<Renderer> resultRenderers;

	private Object controller;
	private Method method;
	private Parameter[] parameters;

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

	void setExceptionHandlers(Map<Class<?>, Renderer> exceptionHandlers) {
		this.exceptionHandlers.clear();
		this.exceptionHandlers.putAll(exceptionHandlers);
	}

	void setParameters(Parameter[] parameters) {
		this.parameters = parameters.clone();
	}

	void setResultRenderers(List<Renderer> resultRenderers) {
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

	public List<Renderer> getResultRenderers() {
		return resultRenderers;
	}

	public Logger log() {
		return log;
	}

	public Set<String> getGroupNames() {
		return groupNames;
	}

	public boolean isRaw() {
		return raw;
	}

	public void invoke(String uri, InvokeContext context) {
		long beginTime = 0;
		if (perfLog.isDebugEnabled()) {
			perfLog.debug("Invoking {}", uri);
			beginTime = System.nanoTime();
		}
		try {
			HttpServletRequest req = context.getRequest();
			HttpServletResponse resp = context.getResponse();

			if (ServletFileUpload.isMultipartContent(req)) {
				try {
					// TODO Rework this to make it configurable and reuseable
					FileItemFactory fileItemFactory = new DiskFileItemFactory(
							DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
							uploadTemp);
					context.setMultipart(true);
					ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
					List<FileItem> items = servletFileUpload.parseRequest(req);
					for (FileItem item : items) {
						MultiPartItem part = new MultiPartItemCommon(item);
						if (!item.isInMemory()) {
							context.registerCloseable(part,
									"UploadFile: " + item.getFieldName() + " name: " + item.getName());
						}
						context.addPart(part);
					}
				} catch (FileUploadException e) {
					throw new RuntimeException(e);
				}
				if (perfLog.isDebugEnabled()) {
					perfLog.debug("Parse multipart in {}ms", (System.nanoTime() - beginTime) / 1000000.0f);
					beginTime = System.nanoTime();
				}
			}

			int parametersCount = parameters.length;
			Object[] invokeParams = new Object[parametersCount];

			try {
				// bind parameter
				for (int i = 0; i < parametersCount; i++) {
					Parameter parameter = parameters[i];
					Class<?> type = parameter.getType();
					Object value = ParameterSource.NOT_HERE;
					for (ParameterSource source : parameter.getParameterSources()) {
						value = source.getParameterValue(context, parameter);
					}
					if (value == ParameterSource.NOT_HERE) {
						value = null;
					}
					if (value == null && type.isPrimitive()) {
						value = defaultPrimitives.get(type);
					}
					invokeParams[i] = value;
				}
			} catch (Throwable t) {
				log.error("Exception occurred binding parameters {}{}", req.getRequestURI(),
						req.getQueryString() != null ? req.getQueryString() : "", t);
				handleException(context, t);
			}
			if (perfLog.isDebugEnabled()) {
				perfLog.debug("Parameter resolve in {}ms", (System.nanoTime() - beginTime) / 1000000.0f);
				beginTime = System.nanoTime();
			}
			try {
				Object result = method.invoke(controller, invokeParams);
				if (perfLog.isDebugEnabled()) {
					perfLog.debug("Controller execution in {}ms", (System.nanoTime() - beginTime) / 1000000.0f);
					beginTime = System.nanoTime();
				}
				handleResult(context, req, resp, result);
				if (perfLog.isDebugEnabled()) {
					perfLog.debug("Result handle in {}ms", (System.nanoTime() - beginTime) / 1000000.0f);
					beginTime = System.nanoTime();
				}
			} catch (Throwable t) {
				log.error("Exception occurred processing request {}{}", req.getRequestURI(),
						req.getQueryString() != null ? req.getQueryString() : "", t);
				handleException(context, t);
			}
		} finally {
			try {
				context.close();
			} catch (Exception e) {
				log.warn("Failed closing context", e);
			}
		}
	}

	public void handleResult(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Object resultObj) {
		if (resultObj == null) {
			return;
		} else if (resultObj instanceof Renderer) {
			Renderer result = (Renderer) resultObj;
			try {
				if (!result.renderResult(ctx, null)) {
					handleException(ctx,
							new IllegalStateException("Can't handle specified renderer: " + resultObj));
				}
			} catch (Exception e) {
				log.error("Exception occurred processing result of request {}{}", req.getRequestURI(),
						req.getQueryString() != null ? req.getQueryString() : "", e);
				handleException(ctx, e);
			}
		} else {
			boolean handled = false;
			for (Renderer renderer : resultRenderers) {
				if (renderer.renderResult(ctx, resultObj)) {
					handled = true;
					break;
				}
			}
			if (!handled) {
				log.error("Exception occurred processing result of request {}{}, Can't handle specified result: {}",
						req.getRequestURI(), req.getQueryString() != null ? req.getQueryString() : "", resultObj);
				handleException(ctx,
						new IllegalStateException("Can't handle specified result: " + resultObj));
			}
		}
	}

	public void handleException(InvokeContext ctx, Throwable e) {
		Class<?> exceptionType = e.getClass();
		Renderer renderer;
		do {
			renderer = exceptionHandlers.get(exceptionType);
			if (renderer != null) {
				break;
			}
		} while (Throwable.class.isAssignableFrom(exceptionType = exceptionType.getSuperclass()));

		if (renderer != null) {
			try {
				renderer.renderResult(ctx, e);
			} catch (Throwable ex) {
				log.error("Exception occored handling exception: {}", e, ex);
			}
		} else {
			try {
				ctx.getResponse().sendError(500, "Server internal error");
			} catch (Throwable ex) {
				log.warn("Can't send error page to client due to IOException, exception is {}", e, ex);
			}
		}
	}
}
