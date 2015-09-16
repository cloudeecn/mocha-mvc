package works.cirno.mocha.mvc.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.mvc.InvokeContext;

public interface ResultRenderer {
	boolean renderResult(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Object resultObj);
}
