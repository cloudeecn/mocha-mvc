package works.cirno.mocha;

import works.cirno.mocha.result.ResultRenderer;

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
	<T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result);

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
	ConfigBuilder renderer(ResultRenderer renderer);

	/**
	 * Bind full http entity from request (aka. request.getInputStream) to an
	 * InputStream
	 * 
	 * @return
	 */
	ConfigBuilder rawEntity();
}