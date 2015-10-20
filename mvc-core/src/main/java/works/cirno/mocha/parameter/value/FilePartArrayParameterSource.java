package works.cirno.mocha.parameter.value;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.MultiPartItem;
import works.cirno.mocha.parameter.name.Parameter;

public class FilePartArrayParameterSource implements ParameterSource {

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		if (!ctx.isMultipart()) {
			return NOT_HERE;
		}

		String name = parameter.getName();
		Class<?> type = parameter.getType();
		if (!type.isArray()) {
			return NOT_HERE;
		}
		MultiPartItem[] parts = ctx.getParts(name);

		if (parts == null || parts.length == 0) {
			return NOT_HERE;
		}
		if (type == MultiPartItem.class) {
			return parts;
		} else if (type == Reader.class || type == InputStream.class) {
			int len = 0;
			for (MultiPartItem part : parts) {
				if (!part.isFormField()) {
					len++;
				}
			}

			if (len == 0) {
				return NOT_HERE;
			}
			if (type == Reader[].class) {
				String encoding = ctx.getCharacterEncoding();
				Reader[] result = new Reader[len];
				int i = 0;
				for (MultiPartItem part : parts) {
					if (!part.isFormField()) {
						try {
							ctx.registerCloseable(
									result[i++] = new InputStreamReader(part.getInputStream(), encoding),
									part.toString());
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException("Encoding " + encoding + " not supported", e);
						} catch (IOException e) {
							throw new RuntimeException("Can't get inputstream from part", e);
						}
					}
				}
				return result;
			} else if (type == InputStream[].class) {
				String encoding = ctx.getCharacterEncoding();
				InputStream[] result = new InputStream[len];
				int i = 0;
				for (MultiPartItem part : parts) {
					if (!part.isFormField()) {
						try {
							ctx.registerCloseable(
									result[i++] = part.getInputStream(),
									part.toString());
						} catch (IOException e) {
							throw new RuntimeException("Can't get inputstream from part", e);
						}
					}
				}
				return result;
			} else if (type == MultiPartItem[].class) {
				MultiPartItem[] result = new MultiPartItem[len];
				int i = 0;
				for (MultiPartItem part : parts) {
					if (!part.isFormField()) {
						result[i++] = part;
					}
				}
				return result;
			}
		}
		return NOT_HERE;
	}

}
