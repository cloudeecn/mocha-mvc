package works.cirno.mocha.mvc.parameter.value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public final class ValueConverterUtil {
    private final static Logger log = LoggerFactory
            .getLogger(ValueConverterUtil.class);
    private final static ConcurrentHashMap<Class<?>, Boolean> notBean = new ConcurrentHashMap<>();

    public static final Collection<? extends ValueConverter> DEFAULT_VALUE_CONVERTERS = Arrays.asList(new DefaultValueConverter());

    public static Object convert(Collection<? extends ValueConverter> valueConverters,
                                 Collection<? extends ParameterSource> parameterSources, Class<?> type,
                                 String key) {
        Object result;

        boolean typeSupported = false;
        for (ValueConverter valueConverter : valueConverters) {
            for (ParameterSource source : parameterSources) {
                result = valueConverter.getValue(type, source, key);
                if (result == ValueConverter.UNSUPPORTED_VALUE) {
                    typeSupported = true;
                } else if (result != ValueConverter.UNSUPPORTED_TYPE) {
                    if (log.isDebugEnabled()) {
                        log.debug("CONV param[{}]({})=({})=>{} ", key,
                                source.getClass(), valueConverter.getClass(),
                                result);
                    }
                    return result;
                }
            }
        }
        return typeSupported ? ValueConverter.UNSUPPORTED_VALUE
                : ValueConverter.UNSUPPORTED_TYPE;
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
                    Class<?> propertyType = pd.getPropertyType();
                    if (propertyType != null) {
                        String propertyName = pd.getName();
                        nameBuilder.setLength(0);
                        nameBuilder.append(paramName).append('.')
                                .append(propertyName);
                        Object value = convert(valueConverters,
                                parameterSources, propertyType,
                                nameBuilder.toString());
                        if (value == ValueConverter.UNSUPPORTED_TYPE
                                || value == ValueConverter.UNSUPPORTED_VALUE) {
                            nameBuilder.setLength(0);
                            nameBuilder.append(propertyName);
                            value = convert(valueConverters, parameterSources,
                                    propertyType, nameBuilder.toString());
                        }
                        if (value != ValueConverter.UNSUPPORTED_TYPE
                                && value != ValueConverter.UNSUPPORTED_VALUE) {
                            // bind
                            Method writeMethod = pd.getWriteMethod();
                            writeMethod.invoke(result, value);
                        }
                    }
                }
                return result;
            } catch (InvocationTargetException | IntrospectionException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
