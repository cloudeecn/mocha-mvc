package works.cirno.mocha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;

class MultiPartItemCommon implements MultiPartItem {

	private FileItem item;

	public MultiPartItemCommon(FileItem item) {
		this.item = item;
	}

	public String getHeader(String name) {
		return item.getHeaders().getHeader(name);
	}

	public Iterator<String> getHeaders(String name) {
		return item.getHeaders().getHeaders(name);
	}

	public Iterator<String> getHeaderNames() {
		return item.getHeaders().getHeaderNames();
	}

	public InputStream getInputStream() throws IOException {
		return item.getInputStream();
	}

	public String getContentType() {
		return item.getContentType();
	}

	public String getName() {
		return item.getName();
	}

	public long getSize() {
		return item.getSize();
	}

	public void write(File file) throws IOException {
		try {
			item.write(file);
		} catch (IOException | Error e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Can't write part to file " + file, e);
		}
	}

	public String getFieldName() {
		return item.getFieldName();
	}

	public boolean isFormField() {
		return item.isFormField();
	}

	public String getString(Charset charset) {
		try {
			return item.getString(charset.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getString(String charset) throws UnsupportedEncodingException {
		return item.getString(charset);
	}

	public String getString() {
		return item.getString();
	}

	@Override
	public void close() throws IOException {
		item.delete();
	}

	@Override
	public String toString() {
		return "MultiPartItem(CommonUpload): " + item;
	}
}
