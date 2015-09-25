package works.cirno.mocha.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.InvokeContext;

public class InputStreamResultRenderer implements ResultRenderer {

	@Override
	public boolean renderResult(InvokeContext ctx, HttpServletRequest req, HttpServletResponse resp, Object resultObj) {
		if (resultObj instanceof InputStream) {
			try (InputStream is = (InputStream) resultObj; OutputStream os = resp.getOutputStream()) {
				byte[] buf = new byte[4096];
				int read;
				while ((read = is.read(buf)) >= 0) {
					os.write(buf, 0, read);
				}
			} catch (IOException e) {
				ctx.getTarget().log().error("Can't write stream to client", e);
			}
			return true;
		}
		return false;
	}

}
