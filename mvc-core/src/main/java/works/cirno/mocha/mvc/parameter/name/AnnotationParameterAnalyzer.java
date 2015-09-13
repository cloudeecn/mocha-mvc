package works.cirno.mocha.mvc.parameter.name;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import works.cirno.mocha.mvc.InvokeTarget;

/**
 *
 */
public class AnnotationParameterAnalyzer implements ParameterAnalyzer {

	@Override
	public Parameter[] getParameters(InvokeTarget target) {
		Method method = target.getMethod();

		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		int length = parameterAnnotations.length;
		Parameter[] parameters = new Parameter[length];
		for (int i = 0; i < length; i++) {
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
		return parameters;
	}
}
