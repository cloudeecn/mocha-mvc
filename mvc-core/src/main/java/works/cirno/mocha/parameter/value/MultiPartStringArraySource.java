package works.cirno.mocha.parameter.value;

import java.nio.charset.Charset;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.MultiPartItem;

public class MultiPartStringArraySource extends AbstractValueSource {

	@Override
	public Object getParameter(InvokeContext ctx, String key) {
		MultiPartItem[] parts = ctx.getParts(key);
		if (parts == null) {
			return NOT_FOUND;
		}
		int len = 0;
		for (MultiPartItem part : parts) {
			if (part.isFormField()) {
				len++;
			}
		}
		if (len == 0) {
			return NOT_FOUND;
		}
		String encoding = ctx.getCharacterEncoding();
		Charset cs = Charset.forName(encoding);
		String[] result = new String[len];
		int i = 0;
		for (MultiPartItem part : parts) {
			if (part.isFormField()) {
				result[i] = part.getString(cs);
				i++;
			}
		}
		return result;
	}

}
