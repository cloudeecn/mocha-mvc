/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2015 Cloudee Huang ( https://github.com/cloudeecn / cloudeecn@gmail.com )
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
