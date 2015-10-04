package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public class RequestArrayParameterSource implements ParameterSource<String[]>{

	@Override
	public Class<String[]> supportsType() {
		return String[].class;
	}

	@Override
	public String[] getParameter(InvokeContext ctx, String key) {
		return ctx.getRequest().getParameterValues(key);
	}



	
}
