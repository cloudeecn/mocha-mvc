package works.cirno.mocha.result;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;

/**
 *
 */
public class CodeResultRenderer implements Renderer {
	private static Logger log = LoggerFactory.getLogger(CodeResultRenderer.class);

	private static final CodeResultRenderer instance = new CodeResultRenderer();

	public static CodeResultRenderer instance() {
		return instance;
	}

	@Override
	public boolean renderResult(InvokeContext ctx, Object resultObj) {
		HttpServletResponse resp = ctx.getResponse();
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
