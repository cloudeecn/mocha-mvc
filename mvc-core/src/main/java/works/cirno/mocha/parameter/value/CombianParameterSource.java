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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class CombianParameterSource implements ParameterSource {

	private static final Logger log = LoggerFactory.getLogger(CombianParameterSource.class);
	private static final Logger perfLog = LoggerFactory.getLogger("perf." + CombianParameterSource.class.getName());

	private Collection<ParameterSource> sources;

	public CombianParameterSource() {

	}

	public CombianParameterSource(Collection<ParameterSource> sources) {
		this.sources = sources;
	}

	public Collection<ParameterSource> getSources() {
		return sources;
	}

	public void setSources(Collection<ParameterSource> sources) {
		this.sources = sources;
	}

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		long paramBeginTime = 0;
		for (ParameterSource source : sources) {
			if (perfLog.isDebugEnabled()) {
				paramBeginTime = System.nanoTime();
			}
			Object value = source.getParameterValue(ctx, parameter);
			if (perfLog.isDebugEnabled()) {
				perfLog.debug("Resolve parameter[{}] by {} in {}ms", parameter.getType().getName(),
						source.getClass().getName(), (System.nanoTime() - paramBeginTime) / 1000000f);
			}
			log.debug("Getting parameter {}, result {}", parameter, value);
			if (value != NOT_HERE) {
				return value;
			}
		}
		return NOT_HERE;
	}

}
