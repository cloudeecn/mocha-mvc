package works.cirno.mocha.parameter.value;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ValueConverterUtil {
	private final static Logger log = LoggerFactory
			.getLogger(ValueConverterUtil.class);
	private final static ConcurrentHashMap<Class<?>, Boolean> notBean = new ConcurrentHashMap<>();

	public static final Collection<? extends ValueConverter> DEFAULT_VALUE_CONVERTERS = Arrays
			.asList(new RawInputStreamConverter(), new PartConverter(), new DefaultValueConverter());

	public static Object convert(Collection<? extends ValueConverter> valueConverters,
			Collection<? extends ParameterSource> parameterSources, Class<?> type,
			String key) {
		Object result = ValueConverter.UNSUPPORTED_TYPE;

		boolean typeSupported = false;
		for (ValueConverter valueConverter : valueConverters) {
			for (ParameterSource source : parameterSources) {
				result = valueConverter.getValue(type, source, key);
				if (result == ValueConverter.UNSUPPORTED_TYPE) {
					break;
				} else if (result == ValueConverter.UNSUPPORTED_VALUE) {
					typeSupported = true;
				} else {
					if (log.isDebugEnabled()) {
						log.debug("CONV param[{}]({})=({})=>{} ", key,
								source.getClass(), valueConverter.getClass(),
								result);
					}
					return result;
				}
			}
		}
		return typeSupported == true ? ValueConverter.UNSUPPORTED_VALUE : result;
	}

	public static Object convertBean(Collection<? extends ValueConverter> valueConverters,
			Collection<? extends ParameterSource> parameterSources, Class<?> type,
			String paramName) {
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
					if(pd.getWriteMethod() != null){
						Class<?> propertyType = pd.getPropertyType();
						if (propertyType != null) {
							String propertyName = pd.getName();
							log.debug("Setting {}.{}...", paramName, propertyName);
							nameBuilder.setLength(0);
							nameBuilder.append(paramName).append('.')
									.append(propertyName);
							String name = nameBuilder.toString();
							log.debug("Setting {}.{}... Trying parameter {}", paramName, propertyName, name);
							Object value = convert(valueConverters,
									parameterSources, propertyType,
									name);
							if (value == ValueConverter.UNSUPPORTED_TYPE
									|| value == ValueConverter.UNSUPPORTED_VALUE) {
								nameBuilder.setLength(0);
								nameBuilder.append(propertyName);
								name = nameBuilder.toString();
								log.debug("Setting {}.{}... Trying parameter {}", paramName, propertyName, name);
								value = convert(valueConverters, parameterSources,
										propertyType, name);
							}
							if (value != ValueConverter.UNSUPPORTED_TYPE
									&& value != ValueConverter.UNSUPPORTED_VALUE) {
								log.debug("Setting {}.{}... Value {} got", paramName, propertyName, value);
								// bind
								Method writeMethod = pd.getWriteMethod();
								writeMethod.invoke(result, value);
							} else {
								log.debug("Setting {}.{}... Value NOT got", paramName, propertyName);
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
