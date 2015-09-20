package works.cirno.mocha.mvc;

import java.util.ArrayList;
import java.util.Collections;

import works.cirno.mocha.mvc.factory.BasicObjectFactory;
import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.AnnotationParameterAnalyzer;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;

/**
 *
 */
public abstract class MVCConfigurator {

	protected static final String SUCCESS = "success";
	protected static final String FAILED = "failed";

	private MVCConfig config = new MVCConfig();
	private ArrayList<ConfigBuilderImpl> configBuilders = new ArrayList<>();

	{
		config.injectBy(defaultObjectFactory());
		config.parameterNamedBy(defaultParameterAnalyzer());
	}

	protected ObjectFactory defaultObjectFactory() {
		return new BasicObjectFactory();
	}

	protected ParameterAnalyzer defaultParameterAnalyzer() {
		return new AnnotationParameterAnalyzer();
	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * A configuration entry looks like this: <br/>
	 * serve("/login").with(LoginController.class,
	 * "loginPage").forward("success").to("/pages/login.jsp")
	 * 
	 * @param path
	 *            The path to serve
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder serve(String path) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, config);
		configBuilders.add(builder);
		return builder;
	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * Likes serve(String path) but this one will serve only specified method,
	 * like GET, POST etc. A configuration entry looks like this: <br/>
	 * serve("/login", "GET").with(LoginController.class,
	 * "loginPage").forward("success").to("/pages/login.jsp")
	 * 
	 * @param path
	 *            The path to serve
	 * @param method
	 *            The method to serve (GET, POST, PUT etc.)
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder serve(String path, String method) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, method, config);
		configBuilders.add(builder);
		return builder;
	}

	/**
	 * Include another MVCConfigurator
	 * 
	 * @param configurator
	 */
	protected void include(MVCConfigurator configurator) {
		configurator.configure();
		configBuilders.addAll(configurator.configBuilders);
	}

	/**
	 * Run code runner in a new configuration scope, handy when lots of entries
	 * shares same config, code like <code>
	 * <pre>all(new Runnable(){
	 * 	public void run(){
	 * 		serve("/api/user/all").with(APIController.class, "list");
	 * 		serve("/api/user/add", "POST").with(APIController.class, "add");
	 * 		serve("/api/user/{name}", "GET").with(APIController.class, "get");
	 * 	}
	 * }).handle(NotAuthorizedException.class, new NotAuthorizedHandler());</pre>
	 * </code>
	 * will make all 3 serves handles NotAuthorizedException with NotAuthorizedHandler <br/>
	 * It's even better in Java 8 with lambda:<code><pre>
	 * all(() -> {
	 * 	serve("/api/user/all").with(APIController.class, "list");
	 * 	serve("/api/user/add", "POST").with(APIController.class, "add");
	 * 	serve("/api/user/{name}", "GET").with(APIController.class, "get");
	 * }).handle(NotAuthorizedException.class, new NotAuthorizedHandler());
	 * </pre></code> 
	 * @param r code to run in 
	 * @return
	 */
	protected ConfigBuilder all(Runnable r) {
		MVCConfig nestedConfig = new MVCConfig(config);
		this.config = nestedConfig;
		r.run();
		config = config.getParent();
		return nestedConfig;
	}

	Iterable<ConfigBuilderImpl> getConfigBuilders() {
		return Collections.unmodifiableCollection(configBuilders);
	}

	public abstract void configure();
}
