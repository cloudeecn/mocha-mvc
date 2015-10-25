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
package works.cirno.mocha.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.InvokeTarget;

/**
 *
 */
public class RegexInvokeTargetResolver implements InvokeTargetResolver {
	private ArrayList<InvokeTargetCriteria> targets = new ArrayList<>();

	@Override
	public InvokeTargetCriteria addServe(String uri, String method, InvokeTarget target) {
		InvokeTargetCriteria criteria = new InvokeTargetCriteria(uri, Pattern.compile(uri), method, target);
		targets.add(criteria);
		return criteria;
	}

	@Override
	public InvokeContext resolve(String uri, String method) {
		for (InvokeTargetCriteria criteria : targets) {
			Pattern pattern = criteria.getPattern();
			Matcher matcher = pattern.matcher(uri);
			if (matcher.matches() && (criteria.getMethod() == null || criteria.getMethod().equals(method))) {
				return new InvokeContext(criteria.getTarget(), matcher, Collections.<String> emptySet());
			}
		}
		return null;
	}
}
