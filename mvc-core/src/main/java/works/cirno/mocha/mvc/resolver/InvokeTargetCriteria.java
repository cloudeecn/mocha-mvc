package works.cirno.mocha.mvc.resolver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import works.cirno.mocha.mvc.InvokeTarget;

/**
 *
 */
public class InvokeTargetCriteria {
	protected static final Pattern GROUP_NAME = Pattern.compile("\\(\\?<(.*?)>");

	private final String uri;
	private final Pattern pattern;
	private final Set<String> groupNames;
	private final String method;
	private final InvokeTarget target;

	public InvokeTargetCriteria(String uri, Pattern pattern, String method, InvokeTarget target) {
		this.uri = uri;
		this.pattern = pattern;
		this.method = method;
		this.target = target;

		Matcher matcher = GROUP_NAME.matcher(uri);
		if (matcher.find()) {
			HashSet<String> groupNames = new HashSet<>(3);
			do {
				groupNames.add(matcher.group(1));
			} while (matcher.find());
			this.groupNames = Collections.unmodifiableSet(groupNames);
		} else {
			groupNames = Collections.<String> emptySet();
		}
	}

	public String getUri() {
		return uri;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getMethod() {
		return method;
	}

	public InvokeTarget getTarget() {
		return target;
	}

	public Set<String> getGroupNames() {
		return groupNames;
	}

}
