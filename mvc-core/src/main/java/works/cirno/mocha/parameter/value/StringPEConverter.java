package works.cirno.mocha.parameter.value;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;

public class StringPEConverter extends AbstractStringArrayConverter {

	private static final Logger log = LoggerFactory.getLogger(StringPEConverter.class);

	@Override
	public Object convertOne(Class<?> type, InvokeContext ctx, Object from) {
		if (from != null && !(from instanceof String)) {
			return NOT_CONVERTABLE;
		}
		PropertyEditor pe = PropertyEditorManager.findEditor(type);
		if (pe == null) {
			return NOT_CONVERTABLE;
		}
		if (from == null) {
			return null;
		} else {
			try {
				pe.setAsText((String) from);
				return pe.getValue();
			} catch (Exception e) {
				log.warn("Can't convert parameter to {}", type.getName(), e);
				return NOT_CONVERTABLE;
			}
		}
	}

}
