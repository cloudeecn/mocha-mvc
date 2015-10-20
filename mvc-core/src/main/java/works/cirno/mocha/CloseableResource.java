package works.cirno.mocha;

import java.io.Closeable;
import java.io.IOException;

final class CloseableResource<T extends Closeable> implements Closeable {

	private final T content;
	private final String name;

	public CloseableResource(T content, String name) {
		super();
		this.content = content;
		this.name = name;
	}

	public T getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	@Override
	public void close() throws IOException {
		content.close();
	}

}
