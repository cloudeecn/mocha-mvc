package works.cirno.mocha.parameter.value;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class RawParameterSource implements ParameterSource {

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		if (ctx.getTarget().isRaw()) {
			Class<?> type = parameter.getType();
			try {
				if (type == InputStream.class) {
					return ctx.getRequest().getInputStream();
				} else if (type == Reader.class) {
					return ctx.getRequest().getReader();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return NOT_HERE;
	}

}
