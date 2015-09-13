package works.cirno.mocha.mvc.parameter.value;

public interface ParameterSource {
	Object getParameter(String key);

	Object[] getParameters(String key);
}
