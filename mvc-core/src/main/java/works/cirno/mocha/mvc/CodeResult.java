package works.cirno.mocha.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class CodeResult implements Result {
    private static Logger log = LoggerFactory.getLogger(CodeResult.class);

    private int code;
    private String msg;

    @Override
    public void renderResult(InvokeTarget target, HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (msg == null) {
                resp.sendError(code);
            } else {
                resp.sendError(code, msg);
            }
        } catch (IOException e) {
            log.warn("Can't send error to client", e);
        }
    }
}
