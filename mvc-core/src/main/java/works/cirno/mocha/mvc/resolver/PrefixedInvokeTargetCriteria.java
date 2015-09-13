package works.cirno.mocha.mvc.resolver;

import works.cirno.mocha.mvc.InvokeTarget;

import java.util.regex.Pattern;

/**
 *
 */
public class PrefixedInvokeTargetCriteria extends InvokeTargetCriteria {
    private final String prefix;

    public PrefixedInvokeTargetCriteria(String uri, Pattern pattern, String method, InvokeTarget target, String prefix) {
        super(uri, pattern, method, target);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
