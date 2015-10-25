/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2015 Cloudee Huang ( https://github.com/cloudeecn / cloudeecn@gmail.com )
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
