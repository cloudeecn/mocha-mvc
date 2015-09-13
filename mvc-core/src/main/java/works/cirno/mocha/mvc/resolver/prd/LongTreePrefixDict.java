package works.cirno.mocha.mvc.resolver.prd;

/**
 *
 */
public class LongTreePrefixDict<T> extends TreePrefixDict<T> implements PrefixDict<T> {

    @Override
    protected TreePrefixDict<T> createNode() {
        return new LongTreePrefixDict<T>();
    }

    @Override
    protected int getNewSize(String prefix) {
        if (this.size == 0) {
            return prefix.length();
        }

        return Math.min(prefix.length(), size);
    }
}
