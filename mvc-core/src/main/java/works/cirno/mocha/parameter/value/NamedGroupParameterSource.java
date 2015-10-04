package works.cirno.mocha.parameter.value;

import java.util.Set;

import works.cirno.mocha.InvokeContext;

/**
 *
 */
public class NamedGroupParameterSource implements ParameterSource<String> {

	public NamedGroupParameterSource() {
	}

	@Override
	public Class<String> supportsType() {
		return String.class;
	}

	@Override
	public String getParameter(InvokeContext ctx, String key) {
		Set<String> names = ctx.getTarget().getGroupNames();
		return (names != null && names.contains(key)) ? ctx.getUriMatcher().group(key) : null;
	}
}
