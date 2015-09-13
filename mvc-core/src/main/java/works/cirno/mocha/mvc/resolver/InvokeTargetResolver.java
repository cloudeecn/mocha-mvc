package works.cirno.mocha.mvc.resolver;

import works.cirno.mocha.mvc.InvokeContext;
import works.cirno.mocha.mvc.InvokeTarget;

/**
 *
 */
public interface InvokeTargetResolver {
    public static final InvokeTarget[] NO_TARGET = new InvokeTarget[0];

    InvokeTargetCriteria addServe(String uri, String method, InvokeTarget target);

    InvokeContext resolve(String uri, String method);
}
