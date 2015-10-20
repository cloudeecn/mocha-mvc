package works.cirno.mocha;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;

public interface MultiPartItem extends Closeable {
	String getHeader(String name);

	Iterator<String> getHeaders(String name);

	Iterator<String> getHeaderNames();

	InputStream getInputStream() throws IOException;

	String getContentType();

	String getName();
	
	String getString(Charset charset);
	
	String getString(String charset) throws UnsupportedEncodingException;
	
	String getString();

	long getSize();

	void write(File file) throws IOException;

	String getFieldName();
	
	boolean isFormField();
}
