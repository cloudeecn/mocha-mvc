package works.cirno.mocha;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

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
		long begin = System.currentTimeMillis();
		log.info("Initializing dispatcher servlet {}", config.getServletName());
		this.config = config;
		try {
			HashMap<String, String> initParameters = new HashMap<>();
			Enumeration<String> keys = config.getInitParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = config.getInitParameter(key);
				initParameters.put(key, value);
			}

			init(config.getServletName(), config.getServletContext(), initParameters);
			log.info("Dispatcher servlet {} initialized in {}ms", config.getServletName(),
					System.currentTimeMillis() - begin);
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
