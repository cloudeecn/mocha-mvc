package works.cirno.mocha.result;

import works.cirno.mocha.InvokeContext;

public interface Renderer {
	boolean renderResult(InvokeContext ctx, Object resultObj);
}
