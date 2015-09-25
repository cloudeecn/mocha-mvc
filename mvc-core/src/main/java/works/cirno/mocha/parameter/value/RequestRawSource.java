package works.cirno.mocha.parameter.value;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class RequestRawSource implements ParameterSource {

	private HttpServletRequest req;

	public RequestRawSource(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public Object getParameter(String key) {
		try {
			return req.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] getParameters(String key) {
		try {
			return new Object[] { req.getInputStream() };
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
