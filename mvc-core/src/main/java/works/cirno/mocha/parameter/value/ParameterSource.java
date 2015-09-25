package works.cirno.mocha.parameter.value;

public interface ParameterSource {
	Object getParameter(String key);

	Object[] getParameters(String key);
}
