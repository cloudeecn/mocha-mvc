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
			@SuppressWarnings("unchecked")
			Enumeration<String> keys = config.getInitParameterNames();
			while (keys.hasMoreElements()) {
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
