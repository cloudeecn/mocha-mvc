package works.cirno.mocha.mvc.parameter.name;

import works.cirno.mocha.mvc.InvokeTarget;

import java.lang.reflect.Method;

/**
 *
 */
public class Java8ParameterAnalyzer implements ParameterAnalyzer {
    @Override
    public Parameter[] getParameters(InvokeTarget target) {
        Method method = target.getMethod();
        java.lang.reflect.Parameter[] jparms = method.getParameters();
        Parameter[] parameters = new Parameter[jparms.length];
        for (int i = 0, max = jparms.length; i < max; i++) {
            java.lang.reflect.Parameter jparm = jparms[i];
            if (jparm.isNamePresent()) {
                parameters[i] = new Parameter(jparm.getName(), jparm.getType());
            } else {
                return null;
            }
        }
        return parameters;
    }
}
