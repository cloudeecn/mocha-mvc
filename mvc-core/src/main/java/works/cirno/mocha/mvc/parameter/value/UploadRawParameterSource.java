package works.cirno.mocha.mvc.parameter.value;

import java.io.InputStream;

public class UploadRawParameterSource implements ParameterSource {
	private InputStream is;

	public UploadRawParameterSource(InputStream is) {
		super();
		this.is = is;
	}

	public Object getParameter(String key) {
		return is;
	}

	public Object[] getParameters(String key) {
		return new InputStream[] { is };
	}

}
