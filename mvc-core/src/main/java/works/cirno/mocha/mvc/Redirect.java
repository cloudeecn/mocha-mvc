package works.cirno.mocha.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

public class Redirect implements Result {

	public final String path;

	public Redirect(String path) {
		this.path = path;
	}

	public void renderResult(InvokeTarget target, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.sendRedirect(this.path);
		} catch (IOException e) {
			LoggerFactory.getLogger(Redirect.class).warn("Can't send redirect to client", e);
		}
	}
}
