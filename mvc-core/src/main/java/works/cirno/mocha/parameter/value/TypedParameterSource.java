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
package works.cirno.mocha.parameter.value;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class TypedParameterSource implements ParameterSource {

	private final ParameterSource NULL_FETCHER = new ParameterSource() {

		@Override
		public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
			return NOT_HERE;
		}
	};
	private final ConcurrentHashMap<Class<?>, ParameterSource> fetchers = new ConcurrentHashMap<>();

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		Class<?> type = parameter.getType();
		ParameterSource fetcher = fetchers.get(type);
		if (fetcher == null) {
			if (type.isAssignableFrom(HttpServletRequest.class) && ServletRequest.class.isAssignableFrom(type)) {
				fetcher = new ParameterSource() {

					@Override
					public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
						return ctx.getRequest();
					}
				};
			} else if (type.isAssignableFrom(HttpServletResponse.class)
					&& ServletResponse.class.isAssignableFrom(type)) {
				fetcher = new ParameterSource() {

					@Override
					public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
						return ctx.getResponse();
					}
				};
			} else if (type.isAssignableFrom(ServletOutputStream.class) && OutputStream.class.isAssignableFrom(type)) {
				fetcher = new ParameterSource() {

					@Override
					public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
						try {
							return ctx.getResponse().getOutputStream();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				};
			} else if (type.isAssignableFrom(PrintWriter.class) && Writer.class.isAssignableFrom(type)) {
				fetcher = new ParameterSource() {

					@Override
					public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
						try {
							return ctx.getResponse().getWriter();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				};
			} else {
				fetcher = NULL_FETCHER;
			}
			fetchers.putIfAbsent(type, fetcher);
		}
		return fetcher.getParameterValue(ctx, parameter);
	}

}
