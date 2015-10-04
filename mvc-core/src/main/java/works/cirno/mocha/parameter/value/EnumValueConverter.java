package works.cirno.mocha.parameter.value;

import java.lang.reflect.Array;

import works.cirno.mocha.InvokeContext;

public class EnumValueConverter implements ValueConverter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getEnumValue(Class<? extends Enum> type, String textValue) {
		return Enum.valueOf(type, textValue);
	}

	@Override
	public Object getValue(InvokeContext ctx, Class<?> type, ParameterSourcePool sourcePool, String key) {
		ParameterSource<?>[] sources;

		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			if (!componentType.isEnum()) {
				return UNSUPPORTED_TYPE;
			}
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumType = componentType.asSubclass(Enum.class);
			// String[] to Enum[]
			sources = sourcePool.getParameterSourcesFor(String[].class);
			for (ParameterSource<?> source : sources) {
				String[] sarr = (String[]) source.getParameter(ctx, key);
				if (sarr != null) {
					int len = sarr.length;
					Object ret = Array.newInstance(componentType, len);
					for (int i = 0; i < len; i++) {
						Array.set(ret, i, getEnumValue(enumType, sarr[i]));
					}
					return ret;
				}
			}

			// String to Enum[]
			sources = sourcePool.getParameterSourcesFor(String.class);
			for (ParameterSource<?> source : sources) {
				String str = (String) source.getParameter(ctx, key);
				if (str != null) {
					Object ret = Array.newInstance(componentType, 1);
					Array.set(ret, 0, getEnumValue(enumType, str));
					return ret;
				}
			}
		} else {
			if (!type.isEnum()) {
				return UNSUPPORTED_TYPE;
			}
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumType = type.asSubclass(Enum.class);
			// String to Enum
			sources = sourcePool.getParameterSourcesFor(String.class);
			for (ParameterSource<?> source : sources) {
				String str = (String) source.getParameter(ctx, key);
				if (str != null) {
					return getEnumValue(enumType, str);
				}
			}

			// String[] to Enum
			sources = sourcePool.getParameterSourcesFor(String[].class);
			for (ParameterSource<?> source : sources) {
				String[] sarr = (String[]) source.getParameter(ctx, key);
				return sarr.length == 0 ? null : getEnumValue(enumType, sarr[0]);
			}
		}
		return null;
	}

}
