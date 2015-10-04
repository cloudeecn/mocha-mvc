package works.cirno.mocha;

import javax.inject.Inject;

import com.google.inject.Injector;

import works.cirno.mocha.DispatcherFilter;
import works.cirno.mocha.ObjectFactory;

public class GuicedDispatcherFilter extends DispatcherFilter {

	@Inject
	private Injector injector;

	@Override
	protected ObjectFactory getMVCFactory() {
		if (injector == null) {
			// not configured in guice servlet, will try fetch injector from
			// servlet context
			final String INJECTOR_NAME = Injector.class.getName();
			injector = (Injector) servletContext.getAttribute(INJECTOR_NAME);
		}

		if (injector == null) {
			throw new IllegalStateException("Can't find guice injector.");
		}
		GuiceMVCFactory factory = new GuiceMVCFactory();
		factory.setInjector(injector);
		return factory;
	}

}
