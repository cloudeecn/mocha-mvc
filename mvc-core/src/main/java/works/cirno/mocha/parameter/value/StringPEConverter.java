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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;

public class StringPEConverter extends AbstractStringArrayConverter {

	private static final Logger log = LoggerFactory.getLogger(StringPEConverter.class);

	@Override
	public Object convertOne(Class<?> type, InvokeContext ctx, Object from) {
		if (from != null && !(from instanceof String)) {
			return NOT_CONVERTABLE;
		}
		PropertyEditor pe = PropertyEditorManager.findEditor(type);
		if (pe == null) {
			return NOT_CONVERTABLE;
		}
		if (from == null) {
			return null;
		} else {
			try {
				pe.setAsText((String) from);
				return pe.getValue();
			} catch (Exception e) {
				log.warn("Can't convert parameter to {}", type.getName(), e);
				return NOT_CONVERTABLE;
			}
		}
	}

}
