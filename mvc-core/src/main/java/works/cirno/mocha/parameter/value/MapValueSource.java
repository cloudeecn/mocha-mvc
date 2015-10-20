package works.cirno.mocha.parameter.value;

import java.util.Map;

import works.cirno.mocha.InvokeContext;

public class MapValueSource extends AbstractValueSource {

	private final Map<String, Object> map;

	public MapValueSource(Map<String, Object> map) {
		super();
		this.map = map;
	}

	@Override
	public Object getParameter(InvokeContext ctx, String key) {
		return map.containsKey(key) ? map.get(key) : NOT_FOUND;
	}

}
