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
package works.cirno.mocha.result;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.InvokeContext;

public class ServletResultRenderer implements Renderer {

	private HashMap<String, Entry<ResultType, String>> results = new HashMap<>();

	public void addResult(String name, ResultType type, String path) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		results.put(name, new SimpleEntry<ResultType, String>(type, path));
	}

	public boolean renderResult(InvokeContext ctx, Object resultObj) {
		HttpServletRequest req = ctx.getRequest();
		HttpServletResponse resp = ctx.getResponse();
		try {
			String name;
			Iterable<Entry<String, Object>> attributes;
			if (resultObj instanceof String) {
				name = (String) resultObj;
				attributes = Collections.emptyList();
			} else if (resultObj instanceof View) {
				View view = (View) resultObj;
				name = view.getName();
				attributes = view.getAttributes();
			} else {
				return false;
			}
			Entry<ResultType, String> result = results.get(name);
			if (result != null) {
				ResultType type = result.getKey();
				String path = result.getValue();
				switch (type) {
				case forward:
					for (Map.Entry<String, Object> attribute : attributes) {
						req.setAttribute(attribute.getKey(), attribute.getValue());
					}
					req.getRequestDispatcher(path).forward(req, resp);
					return true;
				case redirect:
					resp.sendRedirect(path);
					return true;
				}
			}
			return false;
		} catch (ServletException | IOException e) {
			ctx.getTarget().handleException(ctx, e);
			return true;
		}
	}
}
