package works.cirno.mocha.parameter.value;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.MultiPartItem;
import works.cirno.mocha.parameter.name.Parameter;

public class FilePartParameterSource implements ParameterSource {

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		if (!ctx.isMultipart()) {
			return NOT_HERE;
		}
		String name = parameter.getName();
		Class<?> type = parameter.getType();
		if(type.isArray()){
			return NOT_HERE;
		}
		MultiPartItem part = ctx.getPart(name);

		if (part == null) {
			return NOT_HERE;
		} else if (part.isFormField()) {
			return NOT_HERE;
		} else if (type == Reader.class) {
			String encoding = ctx.getCharacterEncoding();
			try {
				Reader result = new InputStreamReader(part.getInputStream(), encoding);
				ctx.registerCloseable(result, part.toString());
				return result;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Encoding " + encoding + " not supported", e);
			} catch (IOException e) {
				throw new RuntimeException("Can't get inputstream from part", e);
			}
		} else if (type == InputStream.class) {
			try {
				InputStream result = part.getInputStream();
				ctx.registerCloseable(result, part.toString());
				return result;
			} catch (IOException e) {
				throw new RuntimeException("Can't get inputstream from part", e);
			}
		} else if (type == MultiPartItem.class) {
			return part;
		} else {
			return NOT_HERE;
		}
	}

}
