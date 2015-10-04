package works.cirno.mocha;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Named
@Singleton
public class SpringMVCFactory implements ObjectFactory, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(SpringMVCFactory.class);
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public <T extends MVCConfigurator> T getMVCConfiguratorInstance(Class<T> clazz) {
		try {
			return applicationContext.getBean(clazz);
		} catch (NoSuchBeanDefinitionException e) {
			log.info("MVCConfigurator bean created");
			return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
		}
	}

	@Override
	public Object getInstance(String name) {
		return applicationContext.getBean(name);
	}

	public static ObjectFactory fromCustomApplicationContext(ApplicationContext ac) {
		ObjectFactory factory;
		try {
			factory = ac.getBean(SpringMVCFactory.class);
			log.info("SpringObjectFactory got from application context");
		} catch (NoSuchBeanDefinitionException e) {
			factory = ac.getAutowireCapableBeanFactory().createBean(SpringMVCFactory.class);
			log.info("SpringObjectFactory created");
		}
		return factory;
	}

	@Override
	public <T> T getInstance(TypeOrInstance<T> param) {
		switch (param.getInjectBy()) {
		case INSTANCE:
			break;
		case NAME_TYPE:
		case TYPE:
		}
		return null;
	}

}
