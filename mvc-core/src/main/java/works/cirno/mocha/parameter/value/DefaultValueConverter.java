package works.cirno.mocha.parameter.value;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Array;

import works.cirno.mocha.InvokeContext;

public class DefaultValueConverter implements ValueConverter {

	private Object getValue(Class<?> type, String textValue) {
		PropertyEditor editor = PropertyEditorManager.findEditor(type);
		Object value;
		try {
			editor.setAsText(textValue);
			value = editor.getValue();
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}

	public Object getValue(InvokeContext ctx, Class<?> type, ParameterSourcePool sourcePool, String key) {
		ParameterSource<?>[] sources;

		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			if (PropertyEditorManager.findEditor(componentType) == null) {
				return UNSUPPORTED_TYPE;
			}
			// String[] to <Type>[]
			sources = sourcePool.getParameterSourcesFor(String[].class);
			for (ParameterSource<?> source : sources) {
				String[] sarr = (String[]) source.getParameter(ctx, key);
				int len = sarr.length;
				Object ret = Array.newInstance(componentType, len);
				for (int i = 0; i < len; i++) {
					Array.set(ret, i, getValue(componentType, sarr[i]));
				}
				return ret;
			}
			// String to <Type>[]
			sources = sourcePool.getParameterSourcesFor(String.class);
			for (ParameterSource<?> source : sources) {
				String str = (String) source.getParameter(ctx, key);
				if (str != null) {
					Object ret = Array.newInstance(componentType, 1);
					Array.set(ret, 0, getValue(componentType, str));
					return ret;
				}
			}
		} else {
			if (PropertyEditorManager.findEditor(type) == null) {
				return UNSUPPORTED_TYPE;
			}
			// String to <Type>
			sources = sourcePool.getParameterSourcesFor(String.class);
			for (ParameterSource<?> source : sources) {
				String str = (String) source.getParameter(ctx, key);
				if (str != null) {
					return getValue(type, str);
				}
			}

			// String[] to <Type>
			sources = sourcePool.getParameterSourcesFor(String[].class);
			for (ParameterSource<?> source : sources) {
				String[] sarr = (String[]) source.getParameter(ctx, key);
				return sarr.length == 0 ? null : getValue(type, sarr[0]);
			}
		}
		return null;
	}

}
