package works.cirno.mocha.mvc;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;

/**
 *
 */
public class InvokeContext {
	private InvokeTarget target;
	private Matcher uriMatcher;
	private Set<String> groupNames;
	private HashMap<String, Object> attributes = new HashMap<>();

	public InvokeContext(InvokeTarget target, Matcher uriMatcher, Set<String> groupNames) {
		this.target = target;
		this.uriMatcher = uriMatcher;
		this.groupNames = groupNames;
	}

	public InvokeTarget getTarget() {
		return target;
	}

	public Matcher getUriMatcher() {
		return uriMatcher;
	}

	public boolean isGroupName(String str) {
		return groupNames.contains(str);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public Object setAttribute(String key, Object value) {
		return attributes.put(key, value);
	}
}
