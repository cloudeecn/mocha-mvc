package works.cirno.mocha.mvc;

public interface ServletResultRendererConfig {
	/**
	 * Forward or redirect to following path
	 * @param path The path to forward or redirect to
	 * @return
	 */
	ConfigBuilder to(String path);
}
