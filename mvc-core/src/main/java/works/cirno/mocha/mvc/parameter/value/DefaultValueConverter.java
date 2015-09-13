package works.cirno.mocha.mvc.parameter.value;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Array;

public class DefaultValueConverter implements ValueConverter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getEnumValue(Class<? extends Enum> type, String textValue) {
		return Enum.valueOf(type, textValue);
	}

	private Object getValue(Class<?> type, String textValue) {
		Object result;
		PropertyEditor editor = PropertyEditorManager.findEditor(type);
		if (editor == null) {
			result = UNSUPPORTED_TYPE;
		}
		Object value;
		try {
			editor.setAsText(textValue);
			value = editor.getValue();
		} catch (NumberFormatException e) {
			value = null;
		}
		result = value == null ? UNSUPPORTED_VALUE : value;
		return result;
	}

	public Object getValue(Class<?> type, ParameterSource source, String key) {
		Object result;
		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			Object[] values = source.getParameters(key);
			if (type.isInstance(values)) {
				result = values;
			} else if (PropertyEditorManager.findEditor(componentType) == null) {
				result = UNSUPPORTED_TYPE;
			} else if (componentType.isArray() || componentType.isPrimitive()) {
				// TODO add primitive type array support
				result = UNSUPPORTED_TYPE;
			} else if (values == null
					|| values.getClass().getComponentType() != String.class) {
				result = UNSUPPORTED_VALUE;
			} else {
				int len = values.length;
				String[] valuesArray = (String[]) values;

				Object[] resultArray = (Object[]) Array.newInstance(
						componentType, len);
				result = resultArray;
				if (Enum.class.isAssignableFrom(componentType)) {
					for (int i = 0; i < len; i++) {
						resultArray[i] = getEnumValue(
								componentType.asSubclass(Enum.class),
								valuesArray[i]);
					}
				} else {
					for (int i = 0; i < len; i++) {
						Object value = getValue(componentType, valuesArray[i]);
						resultArray[i] = (value == UNSUPPORTED_TYPE || value == UNSUPPORTED_VALUE) ? null
								: value;
					}
				}
			}
		} else {
			Object value = source.getParameter(key);
			if (type.isInstance(value)) {
				result = value;
			} else if (value == null || value.getClass() != String.class) {
				result = UNSUPPORTED_VALUE;
			} else if (Enum.class.isAssignableFrom(type)) {
				String textForm = (String) value;
				result = getEnumValue(type.asSubclass(Enum.class), textForm);
			} else {
				String textForm = (String) value;
				result = getValue(type, textForm);
			}
		}
		return result;
	}
}
