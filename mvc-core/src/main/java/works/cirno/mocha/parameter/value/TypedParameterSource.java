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
