package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public class RequestArrayValueSource extends AbstractValueSource {

	@Override
	public Object getParameter(InvokeContext ctx, String key) {
		String[] ret = ctx.getRequest().getParameterValues(key);
		if (ret == null) {
			return NOT_FOUND;
		} else {
			return ret;
		}
	}

}