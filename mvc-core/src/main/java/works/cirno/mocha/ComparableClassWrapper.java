package works.cirno.mocha;

/**
 * <p>Wraps classes in a comparable way</p>
 * 
 * <p>Ensures following rules: <br/>
 * 	Given W(A) is a ComparableClassWrapper with class A as its content<br/>
 * 	<ol>
 * 		<li>W(A).compareTo(W(B)) = 0 when A is same as B</li>
 * 		<li>W(A).compareTo(W(B)) < 0 when A is subclass of B</li>
 * 		<li>If W(A).compareTo(W(B)) < 0 and W(B).compareTo(W(C)) < 0, then W(A).compareTo(W(C)) < 0</li>
 * 	</ol>
 * 	So you can put wrappers in a tree and find nearest parent class or subclass of a specified class in this tree. ( A use case is to find nearest exception type registered in exception handlers table. )
 * </p>
 */
public class ComparableClassWrapper implements Comparable<ComparableClassWrapper> {
    private final Class<?> content;

    public ComparableClassWrapper(Class<?> content) {
        this.content = content;
    }

    public Class<?> getContent(){
        return content;
    }
    @Override
    public int compareTo(ComparableClassWrapper o) {
        Class<?> other = o.content;
        if (other.equals(content)) {
            return 0;
        } else if (other.isAssignableFrom(content)) {
            return -1;
        } else if (content.isAssignableFrom(other)) {
            return 1;
        } else {
            Class<?> commonParent = null;
            Class<?> lastEx = null;
            Class<?> lastOther = null;
            for (Class<?> clz = content; clz != null; clz = clz.getSuperclass()) {
                if (clz.isAssignableFrom(other)) {
                    commonParent = clz;
                    break;
                }
                lastEx = clz;
            }
            if (commonParent == null || lastEx == null) {
                throw new IllegalStateException("Can't find common parent for class " + content.getName() + " and " + other.getName());
            }
            for (Class<?> clz = other; clz != null; clz = clz.getSuperclass()) {
                if (commonParent.equals(clz)) {
                    break;
                }
                lastOther = clz;
            }
            if (lastOther == null) {
                throw new IllegalStateException("Can't find common parent for class " + content.getName() + " and " + other.getName());
            }
            return lastEx.getName().compareTo(lastOther.getName());
        }
    }

    public String toString() {
        return content.getName();
    }
}
