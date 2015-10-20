package works.cirno.mocha.parameter.name;

import java.util.Arrays;
import java.util.Collection;

import works.cirno.mocha.parameter.value.DefaultParameterSource;
import works.cirno.mocha.parameter.value.ParameterSource;

public class Parameter {

	private Collection<ParameterSource> DEFAULT_PARAMETER_SOURCES = Arrays
			.<ParameterSource> asList(DefaultParameterSource.INSTANCE);

	private final String name;
	private final Class<?> type;

	private Collection<ParameterSource> parameterSources = DEFAULT_PARAMETER_SOURCES;

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

	public Collection<ParameterSource> getParameterSources() {
		return parameterSources;
	}

	public void setParameterSources(Collection<ParameterSource> parameterSources) {
		this.parameterSources = parameterSources;
	}

	@Override
	public String toString() {
		return "Parameter[" + name + "](" + type.getName() + ")";
	}

}
