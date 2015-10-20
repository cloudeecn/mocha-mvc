package works.cirno.mocha;

import java.io.File;

import works.cirno.mocha.result.Renderer;

/**
 *
 */
public interface ConfigBuilder {
	/**
	 * Handle request with specified controller and method
	 */
	ConfigBuilder with(String controllerName, String methodName);

	/**
	 * Handle request with specified controller and method
	 */
	ConfigBuilder with(Class<?> controller, String methodName);

	/**
	 * Handle request with specified controller and method
	 */
	ConfigBuilder with(String controllerName);

	/**
	 * Handle request with specified controller and method
	 */
	ConfigBuilder with(Class<?> controller);

	/**
	 * Handle request with specified controller and method
	 */
	ConfigBuilder withMethod(String methodName);

	/**
	 * Handle
	 * 
	 * @param exception
	 * @param result
	 * @return
	 */
	<T extends Throwable> ConfigBuilder exception(Class<T> exception, Renderer result);

	<T extends Throwable> ConfigBuilder exception(Class<T> exception, Class<? extends Renderer> result);

	<T extends Throwable> ConfigBuilder exception(Class<T> exception, String resultName);

	/**
	 * Forward a view result to a specific path (likely forward to JSP) <br/>
	 * Following by a to(path) method, you will specify path to forward to there
	 * 
	 * @param resultName
	 *            "name" in a View object
	 * @return
	 */
	ServletResultRendererConfig forward(String resultName);

	/**
	 * Redirect a view result to a specific path (forward to JSP) <br/>
	 * Following by a to(path) method, you will specify path to redirect to
	 * there
	 * 
	 * @param resultName
	 *            "name" in a View object
	 * @return
	 */
	ServletResultRendererConfig redirect(String resultName);

	/**
	 * Add a custom works.cirno.mocha.result.ResultRenderer to handle result
	 * 
	 * @param renderer
	 * @return
	 */
	ConfigBuilder prependResultRenderer(Renderer renderer);

	ConfigBuilder prependResultRenderer(Class<? extends Renderer> renderer);

	ConfigBuilder prependResultRenderer(String rendererName);

	/**
	 * Add a custom works.cirno.mocha.result.ResultRenderer to handle result
	 * 
	 * @param renderer
	 * @return
	 */
	ConfigBuilder appendResultRenderer(Renderer renderer);

	ConfigBuilder appendResultRenderer(Class<? extends Renderer> renderer);

	ConfigBuilder appendResultRenderer(String rendererName);

	/**
	 * Don't parse http body posted, you can fetch the body from a InputStream
	 * parameter in controller method
	 * 
	 * @return
	 */
	ConfigBuilder raw();

	/**
	 * Set directory which will store temporary uploaded data
	 * 
	 * @param tempDirectory
	 * @return
	 */
	ConfigBuilder uploadTemp(File tempDirectory);

}
