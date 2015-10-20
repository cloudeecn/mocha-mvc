package works.cirno.mocha;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

@Singleton
public class GuiceMVCFactory implements ObjectFactory {

	@Inject
	private Injector injector;

	void setInjector(Injector injector) {
		this.injector = injector;
	}

	@Override
	public <T> T getInstance(TypeOrInstance<T> param) {
		switch (param.getInjectBy()) {
		case INSTANCE:
			return param.getInstance();
		case NAME_TYPE:
			return injector.getInstance(Key.get(param.getType(), Names.named(param.getName())));
		case TYPE:
			return injector.getInstance(param.getType());
		}
		throw new UnsupportedOperationException("Unknown injection type " + param.getInjectBy());
	}
}
