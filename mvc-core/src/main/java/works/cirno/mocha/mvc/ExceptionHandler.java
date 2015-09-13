package works.cirno.mocha.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public interface ExceptionHandler<T extends Throwable> {
    void handle(T exception, HttpServletRequest req, HttpServletResponse resp);
}
