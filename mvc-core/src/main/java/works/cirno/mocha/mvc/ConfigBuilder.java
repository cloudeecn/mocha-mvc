package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.result.ResultRenderer;

/**
 *
 */
public interface ConfigBuilder {
	/**
	 * Handle request with specified controller and method
	 * */
	ConfigBuilder with(Class<?> controller, String methodName);

	/**
	 * Handle 
	 * @param exception
	 * @param result
	 * @return
	 */
	<T extends Throwable> ConfigBuilder exception(Class<T> exception, ResultRenderer result);

	/**
	 * Forward a view result to a specific path (likely forward to JSP) <br/>
	 * Following by a to(path) method, you will specify path to forward to there
	 * @param resultName "name" in a View object
	 * @return
	 */
	ServletResultRendererConfig forward(String resultName);

	/**
	 * Redirect a view result to a specific path (forward to JSP) <br/>
	 * Following by a to(path) method, you will specify path to redirect to there
	 * @param resultName "name" in a View object
	 * @return
	 */
	ServletResultRendererConfig redirect(String resultName);

	/**
	 * Add a custom works.cirno.mocha.mvc.result.ResultRenderer to handle result 
	 * @param renderer
	 * @return
	 */
	ConfigBuilder renderer(ResultRenderer renderer);

	/**
	 * Use custom object factory for controller creation and dependency injection in this scope
	 * 
	 * (Rarely used)
	 * 
	 * @param factory
	 * @return
	 */
	ConfigBuilder injectBy(ObjectFactory factory);
	
	/**
	 * Use custom parameter analyzer to get names from controller's parameters in this scope
	 * 
	 * @param analyzer
	 * @return
	 */
	ConfigBuilder parameterNamedBy(ParameterAnalyzer analyzer);
}
