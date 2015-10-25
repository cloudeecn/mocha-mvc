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
package works.cirno.mocha.parameter.value;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class BeanParameterSource implements ParameterSource {

	private ConcurrentHashMap<Class<?>, Boolean> notBean = new ConcurrentHashMap<Class<?>, Boolean>();

	private ParameterSource propertySource;

	ParameterSource getPropertySource() {
		return propertySource;
	}

	void setPropertySource(ParameterSource propertySource) {
		this.propertySource = propertySource;
	}

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		Class<?> type = parameter.getType();
		String paramName = parameter.getName();
		if (notBean.contains(type)) {
			return NOT_HERE;
		} else {
			try {
				BeanInfo info;
				Constructor<?> constructor;
				try {
					if ((type.getModifiers() & (Modifier.ABSTRACT)) != 0) {
						notBean.put(type, true);
						return NOT_HERE;
					}
					info = Introspector.getBeanInfo(type);
					constructor = type.getConstructor();
					if ((constructor.getModifiers() & (Modifier.PUBLIC)) == 0) {
						notBean.put(type, true);
						return NOT_HERE;
					}
				} catch (NoSuchMethodException | IntrospectionException e) {
					notBean.put(type, true);
					return NOT_HERE;
				}
				Object result = constructor.newInstance();
				PropertyDescriptor[] propertyDescriptors = info
						.getPropertyDescriptors();
				StringBuilder nameBuilder = new StringBuilder(64);
				boolean propertyGot = false;
				
				for (PropertyDescriptor pd : propertyDescriptors) {
					Class<?> propertyType = pd.getPropertyType();
					Method writeMethod = pd.getWriteMethod();
					if (propertyType != null && writeMethod != null) {
						String propertyName = pd.getName();
						nameBuilder.setLength(0);
						nameBuilder.append(paramName).append('.')
								.append(propertyName);

						Parameter propertyParameter = new Parameter(nameBuilder.toString(), propertyType);
						Object value = propertySource.getParameterValue(ctx, propertyParameter);
						if (value != NOT_HERE) {
							propertyGot = true;
							if (writeMethod != null) {
								try{
									writeMethod.invoke(result, value);
								}catch(IllegalArgumentException e){
									throw new IllegalArgumentException(
											"Expected type: " + writeMethod.getParameterTypes()[0].getName()
													+ " actual type: " + value.getClass().getName(),
											e);
								}
							}
						}
					}
				}
				return propertyGot ? result : NOT_HERE;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException ex) {
				throw new RuntimeException("Can't setup bean parameter " + paramName + "(" + type + ")", ex);
			}
		}
	}

}
