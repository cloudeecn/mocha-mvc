package works.cirno.mocha;

/**
 *
 */
public interface ObjectFactory {
	<T> T getInstance(TypeOrInstance<T> param);
}
