package works.cirno.mocha;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Named
@Singleton
public class SpringMVCFactory implements ObjectFactory, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(SpringMVCFactory.class);
	private ApplicationContext applicationContext;

	private ConcurrentHashMap<Class<?>, Object> unmanagedBeans = new ConcurrentHashMap<>(4);

	AutowiredAnnotationBeanPostProcessor processer;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		processer = new AutowiredAnnotationBeanPostProcessor();
		processer.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
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
			return param.getInstance();
		case NAME_TYPE:
			return applicationContext.getBean(param.getName(), param.getType());
		case TYPE:
			try {
				return applicationContext.getBean(param.getType());
			} catch (NoSuchBeanDefinitionException e) {
				Class<? extends T> type = param.getType();
				
				@SuppressWarnings("unchecked")
				T result = (T) unmanagedBeans.get(type);
				if (result == null) {
					log.warn("Can't find bean for type {} will manage bean as singleton", type);
					try {
						result = type.newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						log.error("Can't instantiate {}.", type.getName(), e1);
						throw new NoSuchBeanDefinitionException(type);
					}
					processer.processInjection(result);
					unmanagedBeans.put(type, result);
				}
				return result;
			}
		}
		throw new UnsupportedOperationException("Unknown injection type " + param.getInjectBy());
	}

}
