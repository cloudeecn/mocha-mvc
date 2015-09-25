package works.cirno.mocha.resolver.prd;

import java.util.List;

/**
 *
 */
public interface PrefixDict<T> {
	void add(String prefix, T value);

	List<T> select(String string);

	List<T> select(String string, List<T> values);
}
