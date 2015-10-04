package works.cirno.mocha.parameter.value;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import works.cirno.mocha.InvokeContext;

public class MultiPartParameterSource implements ParameterSource<String> {

	@Override
	public Class<String> supportsType() {
		return String.class;
	}

	@Override
	public String getParameter(InvokeContext ctx, String key) {
		if (ServletFileUpload.isMultipartContent(ctx.getRequest())) {
			
		} else {
			return null;
		}
		FileItemFactory fif = new DiskFileItemFactory();
		return null;
	}

}
