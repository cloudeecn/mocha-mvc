package works.cirno.mocha.mvc.parameter.name;

public class Parameter {

	private final String name;
	private final Class<?> type;

	public Parameter(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

}
