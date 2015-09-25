package works.cirno.mocha.result;

import java.util.HashMap;
import java.util.Map.Entry;

public class View {
	private String name;
	private HashMap<String, Object> attributes = new HashMap<>();

	public View(String name) {
		this.name = name;
	}

	public View attribute(String key, Object value) {
		attributes.put(key, value);
		return this;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getName() {
		return name;
	}

	public Iterable<Entry<String, Object>> getAttributes() {
		return attributes.entrySet();
	}
}
