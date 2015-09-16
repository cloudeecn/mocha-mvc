package works.cirno.mocha.mvc.result;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.mvc.InvokeContext;

/**
 *
 */
public class CodeResultRenderer implements ResultRenderer {
	private static Logger log = LoggerFactory.getLogger(CodeResultRenderer.class);

	@Override
	public boolean renderResult(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Object resultObj) {
		if (resultObj instanceof Number) {
			try {
				resp.sendError(((Number) resultObj).intValue());
			} catch (IOException e) {
				log.warn("Can't send error to client", e);
			}
			return true;
		}
		return false;
	}
}
