package works.cirno.mocha;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import works.cirno.mocha.MVCConfigurator;
import works.cirno.mocha.ObjectFactory;

@Singleton
public class GuiceMVCFactory implements ObjectFactory {

	@Inject
	private Injector injector;

	void setInjector(Injector injector) {
		this.injector = injector;
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public <T extends MVCConfigurator> T getMVCConfiguratorInstance(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public Object getInstance(String name) {
		return injector.getInstance(Key.get(Object.class, Names.named(name)));
	}
}
