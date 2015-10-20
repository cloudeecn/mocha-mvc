package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public class MatcherValueSource extends AbstractValueSource {

	@Override
	public Object getParameter(InvokeContext ctx, String key) {
		if(ctx.isGroupName(key)){
			return ctx.getUriMatcher().group(key);
		}
		return NOT_FOUND;
	}

}
