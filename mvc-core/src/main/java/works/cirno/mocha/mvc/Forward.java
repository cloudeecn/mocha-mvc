package works.cirno.mocha.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Forward implements Result {

    public final String path;
    private HashMap<String, Object> attributes;

    public Forward(String path) {
        this.path = path;
    }

    public void renderResult(InvokeTarget target, HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (attributes != null) {
                for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
                    req.setAttribute(attribute.getKey(), attribute.getValue());
                }
            }
            req.getRequestDispatcher(this.path).forward(req, resp);
        } catch (ServletException | IOException e) {
            target.handleException(e, req, resp);
        }
    }

    public Forward attribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(name, value);
        return this;
    }
}
