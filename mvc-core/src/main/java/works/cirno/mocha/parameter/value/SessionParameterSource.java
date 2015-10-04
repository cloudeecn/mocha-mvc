package works.cirno.mocha.parameter.value;

import javax.servlet.http.HttpSession;

import works.cirno.mocha.InvokeContext;

public class SessionParameterSource implements ParameterSource<HttpSession> {

	@Override
	public Class<HttpSession> supportsType() {
		return HttpSession.class;
	}

	@Override
	public HttpSession getParameter(InvokeContext ctx, String key) {
		return ctx.getRequest().getSession(false);
	}

}
