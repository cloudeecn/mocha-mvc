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
