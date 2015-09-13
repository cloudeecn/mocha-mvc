package works.cirno.mocha.mvc.parameter.value;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterSource implements ParameterSource {

	private final HttpServletRequest req;

	public RequestParameterSource(HttpServletRequest req) {
		super();
		this.req = req;
	}

	public Object getParameter(String key) {
		return req.getParameter(key);
	}

	public Object[] getParameters(String key) {
		return req.getParameterValues(key);
	}

}
