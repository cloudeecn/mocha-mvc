package works.cirno.mocha.mvc.parameter.value;

import java.util.Set;
import java.util.regex.Matcher;

/**
 *
 */
public class NamedGroupParameterSource implements ParameterSource {

	private final Matcher matcher;
	private final Set<String> groupNames;

	public NamedGroupParameterSource(Matcher matcher, Set<String> groupNames) {
		this.matcher = matcher;
		this.groupNames = groupNames;
	}

	@Override
	public Object getParameter(String key) {
		if (groupNames.contains(key)) {
			return matcher.group(key);
		} else {
			return null;
		}
	}

	@Override
	public Object[] getParameters(String key) {
		if (groupNames.contains(key)) {
			Object value = getParameter(key);
			return value == null ? new Object[0] : new Object[] { value };
		} else {
			return null;
		}
	}
}
