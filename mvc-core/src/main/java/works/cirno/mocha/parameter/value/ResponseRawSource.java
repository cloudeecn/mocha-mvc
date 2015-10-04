package works.cirno.mocha.parameter.value;

import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.InvokeContext;

public class ResponseRawSource implements ParameterSource<HttpServletResponse> {

	@Override
	public Class<HttpServletResponse> supportsType() {
		return HttpServletResponse.class;
	}

	@Override
	public HttpServletResponse getParameter(InvokeContext ctx, String key) {
		return ctx.getResponse();
	}

}
