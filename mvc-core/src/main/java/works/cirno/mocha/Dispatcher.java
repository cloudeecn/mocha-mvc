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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.parameter.name.ASMParameterAnalyzer;
import works.cirno.mocha.parameter.name.AnnotationParameterAnalyzer;
import works.cirno.mocha.parameter.name.Parameter;
import works.cirno.mocha.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.resolver.InvokeTargetCriteria;
import works.cirno.mocha.resolver.InvokeTargetResolver;
import works.cirno.mocha.resolver.PrefixDictInvokeTargetResolver;
import works.cirno.mocha.result.Renderer;
import works.cirno.mocha.result.ServletResultRenderer;

/**
 *
 */
public abstract class Dispatcher {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Logger perfLog = LoggerFactory.getLogger("perf." + Dispatcher.class.getName());

	protected ServletContext servletContext;
	protected Map<String, String> initParameters;

	protected Collection<ParameterAnalyzer> parameterAnalyzers = new ArrayList<>();
	protected InvokeTargetResolver invokeTargetResolver = new PrefixDictInvokeTargetResolver();
	protected ObjectFactory factory;

	protected final static Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
	protected final static Pattern regexMeta = Pattern.compile("[$*+?.|\\^\\{\\}\\[\\]\\(\\)\\\\]");

	protected Collection<ParameterAnalyzer> getParameterAnalyzers() {
		return Arrays.asList(
				new ASMParameterAnalyzer(),
				new AnnotationParameterAnalyzer());
	}

	protected InvokeTargetResolver getInvokeTargetResolver() {
		return new PrefixDictInvokeTargetResolver();
	}

	protected ObjectFactory getMVCFactory() {
		return new BasicMVCFactory();
	}

	protected void init(String name, ServletContext sc, Map<String, String> initParameters) throws ServletException {
		this.servletContext = sc;
		this.initParameters = initParameters;

		parameterAnalyzers = getParameterAnalyzers();
		invokeTargetResolver = getInvokeTargetResolver();
		factory = getMVCFactory();

		String configuratorClassName = initParameters.get("configurator");
		if (configuratorClassName == null) {
			throw new ServletException("Can't initialize DispatcherFilter: " + name
					+ ", init parameter [configurator] must be specified");
		}

		Class<? extends MVCConfigurator> configuratorClass;
		try {
			configuratorClass = Class.forName(configuratorClassName).asSubclass(MVCConfigurator.class);
		} catch (ClassNotFoundException e) {
			throw new ServletException(
					"Can't initialize Dispatcher: " + name + ", configurator " + configuratorClassName + " not found",
					e);
		}
		MVCConfigurator configurator = factory
				.getInstance(new TypeOrInstance<>(configuratorClass));

		configurator.setServletContext(sc);
		configurator.configure();

		for (ConfigBuilderImpl cbi : configurator.getConfigBuilders()) {
			TypeOrInstance<?> controllerType = cbi.getController();
			String methodName = cbi.getMethodName();
			Object controller = factory.getInstance(controllerType);

			List<TypeOrInstance<? extends Renderer>> resultRendererTypes = cbi.getResultRenderers();
			ArrayList<Renderer> resultRenderers = new ArrayList<>();
			for (TypeOrInstance<? extends Renderer> type : resultRendererTypes) {
				resultRenderers.add(factory.getInstance(type));
			}

			Map<Class<?>, TypeOrInstance<? extends Renderer>> handlerTypes = cbi.getExceptionHandlers();
			HashMap<Class<?>, Renderer> handlers = new HashMap<>();
			for (Entry<Class<?>, TypeOrInstance<? extends Renderer>> entry : handlerTypes.entrySet()) {
				handlers.put(entry.getKey(), factory.getInstance(entry.getValue()));
			}

			HashMap<String, ServletResultRendererConfigImpl> pendingSRRConfig = cbi
					.getPendingServletResultRendererConfig();
			if (pendingSRRConfig.size() > 0) {
				ServletResultRenderer srr = new ServletResultRenderer();
				for (Entry<String, ServletResultRendererConfigImpl> entry : pendingSRRConfig.entrySet()) {
					srr.addResult(entry.getKey(), entry.getValue().getResultType(), entry.getValue().getPath());
				}
				resultRenderers.add(srr);
			}

			InvokeTarget target = new InvokeTarget(this, controller, methodName);
			target.setResultRenderers(resultRenderers);
			target.setExceptionHandlers(handlers);

			Parameter[] parameters = null;
			for (ParameterAnalyzer pa : parameterAnalyzers) {
				parameters = pa.getParameters(target, parameters);
			}
			target.setParameters(parameters);

			String path = cbi.getPath();
			if (!cbi.isPathRegex()) {
				int lastPos = 0;
				/*
				 * Convert ${xxx} to (?<xxx>.*?)
				 */
				Matcher m = pattern.matcher(path);
				if (m.find()) {
					StringBuffer newPath = new StringBuffer(path.length() + 30);

					do {
						int pos = m.start();

						// before
						if (lastPos != pos) {
							String before = path.substring(lastPos, pos);
							Matcher matcherBefore = regexMeta.matcher(before);
							while (matcherBefore.find()) {
								matcherBefore.appendReplacement(newPath, "\\\\$0");
							}
							matcherBefore.appendTail(newPath);
						}

						// replacement
						newPath.append("(?<").append(m.group(1)).append(">.*?)");
						lastPos = m.end();
					} while (m.find());
					if(lastPos < path.length()){
						// after
						String after = path.substring(lastPos);
						Matcher matcherAfter = regexMeta.matcher(after);
						while (matcherAfter.find()) {
							matcherAfter.appendReplacement(newPath, "\\\\$0");
						}
						matcherAfter.appendTail(newPath);
					}
					path = newPath.toString();
				}
			}
			InvokeTargetCriteria criteria = invokeTargetResolver.addServe(path, cbi.getMethod(), target);
			target.setGroupNames(criteria.getGroupNames());

		}

		Long perfStartTime = (Long) sc.getAttribute(StartupPerformanceListener.KEY_PERF_START);
		if (perfStartTime != null) {
			log.info("{}: Dispatcher initialized {}ms after init start", sc.getContextPath(),
					System.currentTimeMillis() - perfStartTime);
		}
	}

	protected boolean invoke(HttpServletRequest req, HttpServletResponse resp) {
		String uri = req.getRequestURI().substring(req.getContextPath().length());
		long beginTime = 0;
		if (perfLog.isDebugEnabled()) {
			perfLog.debug("Dispatching {}", uri);
			beginTime = System.nanoTime();
		}
		InvokeContext ctx = invokeTargetResolver.resolve(uri, req.getMethod());
		if (perfLog.isDebugEnabled()) {
			perfLog.debug("Target resolved in {}ms", (System.nanoTime() - beginTime) / 1000000.0f);
		}
		if (ctx != null) {
			ctx.setRequest(req);
			ctx.setResponse(resp);
			ctx.getTarget().invoke(uri, ctx);
			return true;
		} else {
			return false;
		}
	}
}
