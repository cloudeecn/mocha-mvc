package works.cirno.mocha.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Result {
	void renderResult(InvokeTarget target, HttpServletRequest req, HttpServletResponse resp);
}
