package works.cirno.mocha.resolver;

import java.util.regex.Pattern;

import works.cirno.mocha.InvokeTarget;

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
