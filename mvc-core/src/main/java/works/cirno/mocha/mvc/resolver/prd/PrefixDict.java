package works.cirno.mocha.mvc.resolver.prd;

import java.util.List;

/**
 *
 */
public interface PrefixDict<T> {
    public void add(String prefix, T value);

    public List<T> select(String string);

    public List<T> select(String string, List<T> values);
}
