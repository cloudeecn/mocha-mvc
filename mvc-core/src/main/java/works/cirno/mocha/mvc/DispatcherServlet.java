package works.cirno.mocha.mvc;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DispatcherServlet extends Dispatcher implements Servlet {

	private static final Logger log = LoggerFactory.getLogger(DispatcherFilter.class);
	private ServletConfig config;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		try {
			String className = config.getInitParameter("configurator");
			if (className == null) {
				throw new ServletException("Can't initialize DispatcherServlet: " + config.getServletName()
						+ ", init parameter [configurator] must be specified");
			}
			Class<? extends MVCConfigurator> configuratorClass;
			try {
				configuratorClass = Class.forName(className).asSubclass(MVCConfigurator.class);
			} catch (ClassNotFoundException e) {
				throw new ServletException("Can't initialize DispatcherServlet: " + config.getServletName()
						+ ", configurator " + className + " not found", e);
			}
			MVCConfigurator configurator;
			try {
				configurator = configuratorClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ServletException("Can't initialize DispatcherServlet: " + config.getServletName()
						+ ", configurator " + className + " can't be instanced", e);
			}
			init(configurator);
		} catch (ServletException e) {
			log.error("Initialization failed", e);
			throw e;
		}
	}

	@Override
	public ServletConfig getServletConfig() {
		return this.config;
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (!this.invoke(req, resp)) {
			resp.sendError(404, "Request path not found");
		}
	}

	@Override
	public String getServletInfo() {
		return "Dispatcher servlet";
	}

	@Override
	public void destroy() {

	}

}
