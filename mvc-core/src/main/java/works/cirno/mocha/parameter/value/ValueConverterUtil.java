package works.cirno.mocha.parameter.value;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;

public final class ValueConverterUtil {
	private final static Logger log = LoggerFactory
			.getLogger(ValueConverterUtil.class);
	private final static ConcurrentHashMap<Class<?>, Boolean> notBean = new ConcurrentHashMap<>();

	public static Object convert(InvokeContext ctx, Collection<? extends ValueConverter> valueConverters,
			ParameterSourcePool sourcePool, Class<?> type,
			String key) {
		Object result = convertSingle(ctx, valueConverters, sourcePool, type, key);
		if (result == ValueConverter.UNSUPPORTED_TYPE) {
			if (type.isPrimitive() || type.isArray() || type.isEnum()) {
				result = null;
			} else {
				result = convertBean(ctx, valueConverters, sourcePool, type, key);
			}
		}
		return result;
	}

	public static Object convertSingle(InvokeContext ctx, Collection<? extends ValueConverter> valueConverters,
			ParameterSourcePool sourcePool, Class<?> type,
			String key) {
		boolean tryAsBean = true;
		for (ValueConverter converter : valueConverters) {
			Object ret = converter.getValue(ctx, type, sourcePool, key);
			if (ret == null) {
				tryAsBean = false;
			} else if (ret != ValueConverter.UNSUPPORTED_TYPE) {
				return ret;
			}
		}
		if (tryAsBean) {
			return ValueConverter.UNSUPPORTED_TYPE;
		}
		return null;
	}

	public static Object convertBean(InvokeContext ctx, Collection<? extends ValueConverter> valueConverters,
			ParameterSourcePool sourcePool, Class<?> type,
			String key) {
		if (notBean.contains(type)) {
			return null;
		} else {
			try {
				try {
					type.getConstructor();
				} catch (NoSuchMethodException e) {
					notBean.put(type, true);
					return null;
				}
				Object result = type.newInstance();
				BeanInfo info = Introspector.getBeanInfo(type);
				PropertyDescriptor[] propertyDescriptors = info
						.getPropertyDescriptors();
				StringBuilder nameBuilder = new StringBuilder(64);
				for (PropertyDescriptor pd : propertyDescriptors) {
					if (pd.getWriteMethod() != null) {
						Class<?> propertyType = pd.getPropertyType();
						if (propertyType != null) {
							String propertyName = pd.getName();
							log.debug("Setting {}.{}...", key, propertyName);
							nameBuilder.setLength(0);
							nameBuilder.append(key).append('.')
									.append(propertyName);
							String name = nameBuilder.toString();
							log.debug("Setting {}.{}... Trying parameter {}", key, propertyName, name);
							Object value = convert(ctx, valueConverters,
									sourcePool, propertyType,
									name);
							if (value != null) {
								log.debug("Setting {}.{}... Value {} got", key, propertyName, value);
								// bind
								Method writeMethod = pd.getWriteMethod();
								writeMethod.invoke(result, value);
							} else {
								log.debug("Setting {}.{}... Value NOT got", key, propertyName);
							}
						}
					}
				}
				return result;
			} catch (InvocationTargetException | IntrospectionException | InstantiationException
					| IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
