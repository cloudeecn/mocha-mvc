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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.InvokeTarget;
import works.cirno.mocha.resolver.prd.LongTreePrefixDict;
import works.cirno.mocha.resolver.prd.PrefixDict;

/**
 *
 */
public class PrefixDictInvokeTargetResolver implements InvokeTargetResolver {
	private static final Pattern REGEX_META = Pattern.compile("[(\\[.\\\\?*+|]");

	private HashMap<String, ArrayList<InvokeTargetCriteria>> fullMatchUris = new HashMap<>();
	private PrefixDict<PrefixedInvokeTargetCriteria> dict;

	public PrefixDictInvokeTargetResolver() {
		this.dict = new LongTreePrefixDict<>();
	}

	public PrefixDictInvokeTargetResolver(PrefixDict<PrefixedInvokeTargetCriteria> dict) {
		this.dict = dict;
	}

	private String getPrefix(String uri) {
		int trimStart = 0;
		int trimEnd = 0;
		if (uri.startsWith("^")) {
			trimStart = 1;
		}
		if (uri.endsWith("$")) {
			trimEnd = 1;
		}
		if (trimStart + trimEnd > 0) {
			uri = uri.substring(trimStart, uri.length() - trimEnd);
		}
		int idx = -1;
		Matcher matcher = REGEX_META.matcher(uri);
		if (matcher.find()) {
			switch (matcher.group().charAt(0)) {
			case '?':
			case '*':
			case '+':
			case '|':
				idx = 0;
				break;
			default:
				idx = matcher.start();
				break;
			}
		}
		switch (idx) {
		case -1:
			return null;
		case 0:
			return "";
		default:
			return uri.substring(0, idx);
		}
	}

	@Override
	public InvokeTargetCriteria addServe(String uri, String method, InvokeTarget target) {
		String prefix = getPrefix(uri);
		if (prefix == null) {
			ArrayList<InvokeTargetCriteria> params = fullMatchUris.get(uri);
			if (params == null) {
				params = new ArrayList<>();
				fullMatchUris.put(uri, params);
			}
			InvokeTargetCriteria criteria = new InvokeTargetCriteria(uri, null, method, target);
			params.add(criteria);
			return criteria;
		} else {
			PrefixedInvokeTargetCriteria criteria = new PrefixedInvokeTargetCriteria(uri,
					Pattern.compile(uri.substring(prefix.length())), method, target, prefix);
			dict.add(prefix, criteria);
			return criteria;
		}
	}

	@Override
	public InvokeContext resolve(String uri, String method) {
		List<InvokeTargetCriteria> fullMatches = fullMatchUris.get(uri);
		if (fullMatches != null) {
			for (InvokeTargetCriteria criteria : fullMatches) {
				if (criteria.getMethod() == null || criteria.getMethod().equals(method)) {
					return new InvokeContext(criteria.getTarget(), null, Collections.<String> emptySet());
				}
			}
		}

		List<PrefixedInvokeTargetCriteria> prefixMatches = dict.select(uri);
		for (int i = prefixMatches.size() - 1; i >= 0; i--) {
			PrefixedInvokeTargetCriteria criteria = prefixMatches.get(i);
			Matcher matcher = criteria.getPattern().matcher(uri.substring(criteria.getPrefix().length()));
			if ((criteria.getMethod() == null || criteria.getMethod().equals(method)) && matcher.matches()) {
				return new InvokeContext(criteria.getTarget(), matcher, criteria.getGroupNames());
			}
		}
		return null;
	}
}
