package works.cirno.mocha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import works.cirno.mocha.result.CodeResultRenderer;
import works.cirno.mocha.result.ResultRenderer;
import works.cirno.mocha.result.ServletResultRenderer;

/**
 *
 */
public abstract class Dispatcher {

	private final Logger log = LoggerFactory.getLogger(getClass());

	protected ServletContext servletContext;
	protected Map<String, String> initParameters;

	protected Collection<ParameterAnalyzer> parameterAnalyzers = new ArrayList<>();
	protected InvokeTargetResolver invokeTargetResolver = new PrefixDictInvokeTargetResolver();
	protected MVCFactory factory;

	protected final static Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");

	protected Collection<ParameterAnalyzer> getParameterAnalyzers() {
		return Arrays.asList(
				new ASMParameterAnalyzer(),
				new AnnotationParameterAnalyzer());
	}

	protected InvokeTargetResolver getInvokeTargetResolver() {
		return new PrefixDictInvokeTargetResolver();
	}

	protected MVCFactory getMVCFactory() {
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
		MVCConfigurator configurator = factory.getMVCConfiguratorInstance(configuratorClass);

		configurator.setServletContext(sc);
		configurator.configure();

		for (ConfigBuilderImpl cbi : configurator.getConfigBuilders()) {

			Class<?> controllerClass = cbi.getControllerClass();
			String controllerName = cbi.getControllerName();
			String methodName = cbi.getMethodName();
			Object controller;
			if (controllerClass != null) {
				controller = factory.getControllerInstance(controllerClass);
			} else if (controllerName != null) {
				controller = factory.getControllerInstance(controllerName);
			} else {
				throw new IllegalStateException(
						"You must either specify controller class or controller name in serve.with for path "
								+ cbi.getPath());
			}
			ArrayList<ResultRenderer> resultRenderers = cbi.getResultRenderers();
			resultRenderers.add(CodeResultRenderer.instance());

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
			target.setExceptionHandlers(cbi.getHandlers());

			Parameter[] parameters = null;
			for (ParameterAnalyzer pa : parameterAnalyzers) {
				parameters = pa.getParameters(target, parameters);
			}
			target.setParameters(parameters);

			String path = cbi.getPath();
			/*
			 * Convert ${xxx} to (?<xxx>.*?)
			 */
			Matcher m = pattern.matcher(path);
			if (m.find()) {
				StringBuffer newPath = new StringBuffer(path.length() + 30);
				do {
					m.appendReplacement(newPath, "(?<$1>.*?)");
				} while (m.find());
				m.appendTail(newPath);
				path = newPath.toString();
			}
			InvokeTargetCriteria criteria = invokeTargetResolver.addServe(cbi.getPath(), cbi.getMethod(), target);
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
		InvokeContext ctx = invokeTargetResolver.resolve(uri, req.getMethod());
		if (ctx != null) {
			ctx.getTarget().invoke(ctx, req, resp);
			return true;
		} else {
			return false;
		}
	}
}
