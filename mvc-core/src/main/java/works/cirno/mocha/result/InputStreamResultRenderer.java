package works.cirno.mocha.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import works.cirno.mocha.InvokeContext;

public class InputStreamResultRenderer implements Renderer {

	@Override
	public boolean renderResult(InvokeContext ctx, Object resultObj) {
		HttpServletResponse resp = ctx.getResponse();
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
