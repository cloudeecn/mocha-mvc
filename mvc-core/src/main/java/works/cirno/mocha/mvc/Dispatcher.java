package works.cirno.mocha.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.resolver.InvokeTargetCriteria;
import works.cirno.mocha.mvc.resolver.InvokeTargetResolver;
import works.cirno.mocha.mvc.resolver.RegexInvokeTargetResolver;
import works.cirno.mocha.mvc.result.CodeResultRenderer;
import works.cirno.mocha.mvc.result.ResultRenderer;
import works.cirno.mocha.mvc.result.ServletResultRenderer;

/**
 *
 */
public abstract class Dispatcher {

	private InvokeTargetResolver resolver = new RegexInvokeTargetResolver();

	protected void init(MVCConfigurator configurator) {
		configurator.configure();

		for (ConfigBuilderImpl cbi : configurator.getConfigBuilders()) {

			Class<?> controllerClass = cbi.getController();
			String methodName = cbi.getMethodName();
			ObjectFactory factory = cbi.getObjectFactory();
			Object controller = factory.getInstance(controllerClass);

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

			InvokeTarget target = new InvokeTarget(this, controllerClass, methodName, controller);
			target.setResultRenderers(resultRenderers);
			target.setExceptionHandlers(cbi.getHandlers());
			target.setParameters(cbi.getParameterAnalyzer().getParameters(target));
			InvokeTargetCriteria criteria = resolver.addServe(cbi.getPath(), cbi.getMethod(), target);
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
