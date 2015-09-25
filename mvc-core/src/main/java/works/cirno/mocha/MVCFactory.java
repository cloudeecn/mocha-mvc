package works.cirno.mocha;

/**
 *
 */
public interface MVCFactory {
	Object getControllerInstance(String name);

	<T> T getControllerInstance(Class<T> clazz);

	<T extends MVCConfigurator> T getMVCConfiguratorInstance(Class<T> clazz);
}
