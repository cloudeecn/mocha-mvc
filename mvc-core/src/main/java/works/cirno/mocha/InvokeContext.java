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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class InvokeContext implements Closeable {
	private static final Logger log = LoggerFactory.getLogger(InvokeContext.class);
	private InvokeTarget target;
	private Matcher uriMatcher;
	private Set<String> groupNames;
	private HashMap<String, Object> attributes;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private boolean multipart;
	private HashMap<String, ArrayList<MultiPartItem>> parts;
	private String characterEncoding = "UTF-8"; // TODO make it configurable
	private ArrayList<CloseableResource<?>> closeables;

	public InvokeContext(InvokeTarget target, Matcher uriMatcher, Set<String> groupNames) {
		this.target = target;
		this.uriMatcher = uriMatcher;
		this.groupNames = groupNames;
	}

	public InvokeTarget getTarget() {
		return target;
	}

	public Matcher getUriMatcher() {
		return uriMatcher;
	}

	public boolean isGroupName(String str) {
		return groupNames.contains(str);
	}

	public Object getAttribute(String key) {
		return attributes == null ? null : attributes.get(key);
	}

	public Object setAttribute(String key, Object value) {
		if (attributes == null) {
			attributes = new HashMap<>();
		}
		return attributes.put(key, value);
	}

	public void addPart(MultiPartItem part) {
		if (parts == null) {
			parts = new HashMap<>();
		}
		String name = part.getFieldName();
		ArrayList<MultiPartItem> list = parts.get(name);
		if (list == null) {
			list = new ArrayList<>(1);
			parts.put(name, list);
		}
		list.add(part);
	}

	public MultiPartItem getPart(String name) {
		if (parts == null) {
			return null;
		}
		ArrayList<MultiPartItem> list = parts.get(name);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	public MultiPartItem[] getParts(String name) {
		if (parts == null) {
			return null;
		}
		ArrayList<MultiPartItem> list = parts.get(name);
		return list == null ? null : list.toArray(new MultiPartItem[list.size()]);
	}

	public HttpServletRequest getRequest() {
		return req;
	}

	void setRequest(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getResponse() {
		return resp;
	}

	void setResponse(HttpServletResponse resp) {
		this.resp = resp;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public void registerCloseable(Closeable closeable, String name) {
		if (closeables == null) {
			closeables = new ArrayList<>();
		}
		closeables.add(new CloseableResource<Closeable>(closeable, name));
	}

	public boolean isMultipart() {
		return multipart;
	}

	void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}

	@Override
	public void close() throws IOException {
		if (closeables != null) {
			for (int i = closeables.size() - 1; i >= 0; i--) {
				CloseableResource<?> closeable = closeables.get(i);
				log.debug("Closing " + closeable.getName());
				try {
					closeable.close();
				} catch (Exception e) {
					log.warn("Can't close resource: " + closeable.getName() + " ("
							+ closeable.getContent().getClass().getName() + ")", e);
				}
			}
		}
	}

}
