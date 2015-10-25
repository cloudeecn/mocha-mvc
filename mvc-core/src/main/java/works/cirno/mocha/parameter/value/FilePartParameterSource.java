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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.MultiPartItem;
import works.cirno.mocha.parameter.name.Parameter;

public class FilePartParameterSource implements ParameterSource {

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		if (!ctx.isMultipart()) {
			return NOT_HERE;
		}
		String name = parameter.getName();
		Class<?> type = parameter.getType();
		if(type.isArray()){
			return NOT_HERE;
		}
		MultiPartItem part = ctx.getPart(name);

		if (part == null) {
			return NOT_HERE;
		} else if (part.isFormField()) {
			return NOT_HERE;
		} else if (type == Reader.class) {
			String encoding = ctx.getCharacterEncoding();
			try {
				Reader result = new InputStreamReader(part.getInputStream(), encoding);
				ctx.registerCloseable(result, part.toString());
				return result;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Encoding " + encoding + " not supported", e);
			} catch (IOException e) {
				throw new RuntimeException("Can't get inputstream from part", e);
			}
		} else if (type == InputStream.class) {
			try {
				InputStream result = part.getInputStream();
				ctx.registerCloseable(result, part.toString());
				return result;
			} catch (IOException e) {
				throw new RuntimeException("Can't get inputstream from part", e);
			}
		} else if (type == MultiPartItem.class) {
			return part;
		} else {
			return NOT_HERE;
		}
	}

}
