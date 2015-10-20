package works.cirno.mocha.parameter.value;

import java.nio.charset.Charset;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.MultiPartItem;

public class MultiPartStringSource extends AbstractValueSource {

	@Override
	public Object getParameter(InvokeContext ctx, String key) {
		MultiPartItem part = ctx.getPart(key);
		if (part != null && part.isFormField()) {
			String encoding = ctx.getCharacterEncoding();
			return part.getString(Charset.forName(encoding));
		} else {
			return NOT_FOUND;
		}
	}

}
