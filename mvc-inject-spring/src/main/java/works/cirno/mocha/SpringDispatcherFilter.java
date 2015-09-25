package works.cirno.mocha;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import works.cirno.mocha.DispatcherFilter;
import works.cirno.mocha.MVCFactory;

public class SpringDispatcherFilter extends DispatcherFilter {

	@Override
	protected MVCFactory getMVCFactory() {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		return SpringMVCFactory.fromCustomApplicationContext(ctx);
	}

}
