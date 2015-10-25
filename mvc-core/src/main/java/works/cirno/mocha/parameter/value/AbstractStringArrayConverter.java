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

import java.lang.reflect.Array;

import works.cirno.mocha.InvokeContext;

public abstract class AbstractStringArrayConverter implements ValueConverter {

	public static Class<?> getArrayType(Class<?> componentType) throws ClassNotFoundException {
		String name;
		if (componentType.isArray()) {
			// just add a leading "["
			name = "[" + componentType.getName();
		} else if (componentType.isPrimitive()) {
			if (componentType == boolean.class) {
				name = "[Z";
			} else if (componentType == byte.class) {
				name = "[B";
			} else if (componentType == char.class) {
				name = "[C";
			} else if (componentType == double.class) {
				name = "[D";
			} else if (componentType == float.class) {
				name = "[F";
			} else if (componentType == int.class) {
				name = "[I";
			} else if (componentType == long.class) {
				name = "[J";
			} else if (componentType == short.class) {
				name = "[S";
			} else {
				throw new UnsupportedOperationException("Unsupported component type: " + componentType.getName());
			}
		} else {
			name = "[L" + componentType.getName() + ";";
		}
		return Class.forName(name);
	}

	@Override
	public Object convert(Class<?> type, InvokeContext ctx, Object from) {
		if (type.isArray()) {
			if (from == null || Array.getLength(from) == 0) {
				Object one = convertOne(type.getComponentType(), ctx, null);
				if (one == NOT_CONVERTABLE) {
					return NOT_CONVERTABLE;
				} else if (one == null) {
					return Array.newInstance(type.getComponentType(), 0);
				} else {
					Object ret = Array.newInstance(type.getComponentType(), 1);
					Array.set(ret, 0, one);
					return ret;
				}
			} else {
				Class<?> componentType = type.getComponentType();
				Object one = convertOne(componentType, ctx, Array.get(from, 0));
				if (one == NOT_CONVERTABLE) {
					return NOT_CONVERTABLE;
				} else {
					int len = Array.getLength(from);
					Object ret = Array.newInstance(componentType, len);
					Array.set(ret, 0, one);
					for (int i = 1; i < len; i++) {
						one = convertOne(componentType, ctx, Array.get(from, i));
						if (one == NOT_CONVERTABLE) {
							return NOT_CONVERTABLE;
						}
						Array.set(ret, 0, one);
					}
					return ret;
				}
			}
		} else {
			return convertOne(type, ctx, from);
		}
	}

	protected abstract Object convertOne(Class<?> type, InvokeContext ctx, Object from);
}
