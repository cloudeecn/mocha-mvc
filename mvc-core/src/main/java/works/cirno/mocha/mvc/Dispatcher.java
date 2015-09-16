package works.cirno.mocha.mvc;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.resolver.InvokeTargetCriteria;
import works.cirno.mocha.mvc.resolver.InvokeTargetResolver;
import works.cirno.mocha.mvc.resolver.RegexInvokeTargetResolver;

/**
 *
 */
public abstract class Dispatcher {

	private InvokeTargetResolver resolver = new RegexInvokeTargetResolver();

	protected void init(MVCConfigurator configurator) {
		configurator.configure();
		configurator.finishConfigure();

		ObjectFactory factory = configurator.getObjectFactory();
		ParameterAnalyzer parameterAnalyzer = configurator.getParameterAnalyzer();

		for (ConfigBuilderImpl cbi : configurator.getConfigBuilders()) {
			cbi.finishConfigure();

			Class<?> controllerClass = cbi.controller;
			String methodName = cbi.methodName;
			Object controller = factory.getInstance(controllerClass);

			InvokeTarget target = new InvokeTarget(this, controllerClass, methodName, controller);
			target.setResultRenderers(cbi.resultRenderers);
			target.setExceptionHandlers(cbi.handlers);
			target.setParameters(parameterAnalyzer.getParameters(target));
			InvokeTargetCriteria criteria = resolver.addServe(cbi.path, cbi.method, target);
			target.setGroupNames(criteria.getGroupNames());
		}
	}

	protected boolean invoke(HttpServletRequest req, HttpServletResponse resp) {
		String uri = req.getRequestURI().substring(req.getContextPath().length());
		InvokeContext ctx = resolver.resolve(uri, req.getMethod());
		if (ctx != null) {
			ctx.getTarget().invoke(ctx, req, resp);
			return true;
		} else {
			return false;
		}
	}
}
