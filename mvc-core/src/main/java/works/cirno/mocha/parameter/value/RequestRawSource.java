package works.cirno.mocha.parameter.value;

import javax.servlet.http.HttpServletRequest;

import works.cirno.mocha.InvokeContext;

public class RequestRawSource implements ParameterSource<HttpServletRequest> {

	public RequestRawSource(HttpServletRequest req) {
	}

	@Override
	public Class<HttpServletRequest> supportsType() {
		return HttpServletRequest.class;
	}

	@Override
	public HttpServletRequest getParameter(InvokeContext ctx, String key) {
		return ctx.getRequest();
	}

}
