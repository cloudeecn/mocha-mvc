package works.cirno.mocha.mvc.parameter.name;

import works.cirno.mocha.mvc.InvokeTarget;

/**
 *
 */
public interface ParameterAnalyzer {
    Parameter[] getParameters(InvokeTarget target);
}
