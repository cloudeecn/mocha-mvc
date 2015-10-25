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

import javax.inject.Inject;

import com.google.inject.Injector;

import works.cirno.mocha.DispatcherFilter;
import works.cirno.mocha.ObjectFactory;

public class GuicedDispatcherFilter extends DispatcherFilter {

	@Inject
	private Injector injector;

	@Override
	protected ObjectFactory getMVCFactory() {
		if (injector == null) {
			// not configured in guice servlet, will try fetch injector from
			// servlet context
			final String INJECTOR_NAME = Injector.class.getName();
			injector = (Injector) servletContext.getAttribute(INJECTOR_NAME);
		}

		if (injector == null) {
			throw new IllegalStateException("Can't find guice injector.");
		}
		GuiceMVCFactory factory = new GuiceMVCFactory();
		factory.setInjector(injector);
		return factory;
	}

}
