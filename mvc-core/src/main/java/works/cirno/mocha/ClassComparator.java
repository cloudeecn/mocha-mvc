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
package works.cirno.mocha;

import java.util.Comparator;

/**
 * <p>Compares class by hierarchy, </p>
 * 
 * <p>Ensures following rules: <br/>
 * 	Given A and B are classes<br/>
 * 	<ol>
 * 		<li>compare(A, B) = 0 when A is same as B</li>
 * 		<li>compare(A, B) < 0 when A is subclass of B</li>
 * 		<li>If compare(A, B) <= 0 and compare(B, C) <= 0, then compare(A, C) <= 0</li>
 * 	</ol>
 */
public class ClassComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		if (o2.equals(o1)) {
			return 0;
		} else if (o2.isAssignableFrom(o1)) {
			return -1;
		} else if (o1.isAssignableFrom(o2)) {
			return 1;
		} else {
			Class<?> commonParent = null;
			Class<?> lastEx = null;
			Class<?> lastOther = null;
			for (Class<?> clz = o1; clz != null; clz = clz.getSuperclass()) {
				if (clz.isAssignableFrom(o2)) {
					commonParent = clz;
					break;
				}
				lastEx = clz;
			}
			if (commonParent == null || lastEx == null) {
				throw new IllegalStateException(
						"Can't find common parent for class " + o1.getName() + " and " + o2.getName());
			}
			for (Class<?> clz = o2; clz != null; clz = clz.getSuperclass()) {
				if (commonParent.equals(clz)) {
					break;
				}
				lastOther = clz;
			}
			if (lastOther == null) {
				throw new IllegalStateException(
						"Can't find common parent for class " + o1.getName() + " and " + o2.getName());
			}
			return lastEx.getName().compareTo(lastOther.getName());
		}
	}

}
