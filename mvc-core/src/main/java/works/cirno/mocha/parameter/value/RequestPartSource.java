package works.cirno.mocha.parameter.value;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class RequestPartSource implements ParameterSource {

	public static boolean isMultipart(HttpServletRequest req) {
		return "multipart/form-data".equals(req.getContentType());
	}

	private HttpServletRequest req;

	public RequestPartSource(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public Object getParameter(String key) {
		try {
			return req.getPart(key);
		} catch (ServletException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] getParameters(String key) {
		Object part = getParameter(key);
		return part == null ? null : new Object[] { part };
	}

}
