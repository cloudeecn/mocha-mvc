package works.cirno.mocha.parameter.name;

import works.cirno.mocha.InvokeTarget;

/**
 *
 */
public interface ParameterAnalyzer {
	Parameter[] getParameters(InvokeTarget target);

	Parameter[] getParameters(InvokeTarget target, Parameter[] parameters);
}
