package works.cirno.mocha;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
public class DispatcherFilter extends Dispatcher implements Filter {

	private static final Logger log = LoggerFactory.getLogger(DispatcherFilter.class);

	@Override
	public void init(FilterConfig config) throws ServletException {
		long begin = System.currentTimeMillis();
		log.info("Initializing dispatcher servlet {}", config.getFilterName());
		try {
			HashMap<String, String> initParameters = new HashMap<>();
			Enumeration<String> keys = config.getInitParameterNames();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = config.getInitParameter(key);
				initParameters.put(key, value);
			}
			
			init(config.getFilterName(), config.getServletContext(), initParameters);
			log.info("Dispatcher filter {} initialized in {}ms", config.getFilterName(),
					System.currentTimeMillis() - begin);
		} catch (ServletException e) {
			log.error("Initialization failed", e);
			throw e;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (!invoke(req, resp)) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}
}
