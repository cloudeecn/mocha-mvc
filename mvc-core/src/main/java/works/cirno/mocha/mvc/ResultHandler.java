package works.cirno.mocha.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ResultHandler<T extends Throwable> implements ExceptionHandler<T> {

	private final Result result;

	private InvokeTarget target;

	public ResultHandler(Result result) {
		this.result = result;
	}

	void setTarget(InvokeTarget target) {
		this.target = target;
	}

	@Override
	public void handle(T exception, HttpServletRequest req, HttpServletResponse resp) {
		result.renderResult(target, req, resp);
	}
}
