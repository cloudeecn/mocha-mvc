package works.cirno.mocha.parameter.name;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import works.cirno.mocha.InvokeTarget;

/**
 *
 */
public class AnnotationParameterAnalyzer implements ParameterAnalyzer {

	@Override
	public Parameter[] getParameters(InvokeTarget target) {
		return getParameters(target, null);
	}

	@Override
	public Parameter[] getParameters(InvokeTarget target, Parameter[] parameters) {
		Method method = target.getMethod();

		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		int length = parameterAnnotations.length;
		if (parameters == null) {
			parameters = new Parameter[length];
		} else if (parameters.length != length) {
			throw new IllegalArgumentException("Input parameters' length is different than parameters of target");
		}
		for (int i = 0; i < length; i++) {
			if (parameters[i] == null || parameters[i].getName() == null) {
				Class<?> type = parameterTypes[i];
				for (Annotation annotation : parameterAnnotations[i]) {
					if (annotation instanceof NamedParam) {
						NamedParam np = ((NamedParam) annotation);
						String name = np.value();
						parameters[i] = new Parameter(name, type);
						break;
					}
				}
				if (parameters[i] == null) {
					parameters[i] = new Parameter(null, type);
				}
			}
		}
		return parameters;
	}
}
