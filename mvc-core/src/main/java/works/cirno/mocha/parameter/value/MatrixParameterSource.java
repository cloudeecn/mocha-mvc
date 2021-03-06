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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class MatrixParameterSource implements ParameterSource {

	private static final Logger log = LoggerFactory.getLogger(MatrixParameterSource.class);
	private static final Logger perfLog = LoggerFactory.getLogger("perf." + MatrixParameterSource.class.getName());

	private List<ValueSource> sources;
	private List<ValueConverter> converters;

	public List<ValueSource> getSources() {
		return sources;
	}

	public void setSources(List<ValueSource> sources) {
		this.sources = sources;
	}

	public List<ValueConverter> getConverters() {
		return converters;
	}

	public void setConverters(List<ValueConverter> converters) {
		this.converters = converters;
	}

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		long beginTime = 0;
		boolean usePerfLog = perfLog.isDebugEnabled();
		Class<?> type = parameter.getType();
		String name = parameter.getName();
		for (ValueSource source : sources) {
			if (usePerfLog) {
				beginTime = System.nanoTime();
			}
			Object sourceValue = source.getParameter(ctx, name);
			if (usePerfLog) {
				perfLog.debug("Get parameter-value named[{}] from source {} in {}ms", parameter.getName(),
						source.getClass().getName(), (System.nanoTime() - beginTime) / 1000000f);
			}
			log.debug("Get parameter[{}]({}) from {}: {}", name, type.getName(), source.getClass().getName(),
					sourceValue);
			if (sourceValue != ValueSource.NOT_FOUND) {

				Object value = ValueConverter.NOT_CONVERTABLE;
				for (ValueConverter converter : converters) {
					if (usePerfLog) {
						beginTime = System.nanoTime();
					}
					value = converter.convert(type, ctx, sourceValue);
					if (usePerfLog) {
						perfLog.debug("Convert parameter-value named[{}] by converter {} in {}ms", parameter.getName(),
								converter.getClass().getName(), (System.nanoTime() - beginTime) / 1000000f);
					}
					if (value != ValueConverter.NOT_CONVERTABLE) {
						return value;
					}
				}
				switch (source.getConvertStrategy()) {
				case EXCEPTION_WHEN_UNCONVERTABLE:
					throw new RuntimeException(
							String.format("Can't find converter to convert parameter[%s] from %s to %s", name,
									sourceValue == null ? "null" : sourceValue.getClass().getName(), type.getName()));
				case NOT_FOUND_WHEN_UNCONVERTABLE:
					return NOT_HERE;
				case SKIP_WHEN_UNCONVERTABLE:
					break;
				case NULL_WHEN_UNCONVERTABLE:
				default:
					log.warn("Can't find converter to convert parameter[{}] from {} to {}", name,
							sourceValue == null ? "null" : sourceValue.getClass().getName(), type.getName());
					return null;
				}
			}
		}
		log.debug("Can't find value for parameter[{}]", name);
		return NOT_HERE;
	}

}
