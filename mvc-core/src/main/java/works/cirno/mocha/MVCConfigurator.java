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

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletContext;

/**
 *
 */
public abstract class MVCConfigurator {

	protected static final String SUCCESS = "success";
	protected static final String FAILED = "failed";

	final MVCConfig defaultConfig = new MVCConfig();
	private MVCConfig config = defaultConfig;
	private ArrayList<ConfigBuilderImpl> configBuilders = new ArrayList<>();
	protected ServletContext servletContext;

	void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * A configuration entry looks like this: <br/>
	 * <code>
	 * 	serve("/login").with(LoginController.class, "loginPage").forward("success").to("/pages/login.jsp")
	 * </code>
	 *
	 * <p>
	 * You can use a simple expression ${parameterName} in path, to make part of
	 * the URL a parameter candidate
	 * </p>
	 * 
	 * <p>
	 * <strong>For example:</strong>
	 * </p>
	 * With following configuration code:<br />
	 * <code> serve("/user/${name}")... </code><br />
	 * And following controller method:<br />
	 * <code> public void example(String name){ </code><br />
	 * When you acccess /user/somename, the <i>name</i> parameter will have
	 * value <i>somename</i>
	 * 
	 * @param path
	 *            The path to serve
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder serve(String path) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, false, config);
		configBuilders.add(builder);
		return builder;
	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * This is like <code>serve(String path)</code> but this one will serve only
	 * specified method, like GET, POST etc. A configuration entry looks like
	 * this: <br/>
	 * <code>
	 * 	serve("/login", "GET").with(LoginController.class, "loginPage").forward("success").to("/pages/login.jsp")
	 * </code>
	 * 
	 * <p>
	 * You can use a simple expression ${parameterName} in path, to make part of
	 * the URL a parameter candidate
	 * </p>
	 * 
	 * <p>
	 * <strong>For example:</strong>
	 * </p>
	 * With following configuration code:<br/>
	 * <code> serve("/user/${name}")... </code><br/>
	 * And following controller method:<br/>
	 * <code> public void example(String name){ </code><br/>
	 * When you acccess /user/somename, the <i>name</i> parameter will have
	 * value <i>somename</i>
	 * 
	 * @param path
	 *            The path to serve
	 * @param method
	 *            The method to serve (GET, POST, PUT etc.)
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder serve(String path, String method) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, false, method, config);
		configBuilders.add(builder);
		return builder;
	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * A configuration entry looks like this: <br/>
	 * <code>
	 * serve("/login").with(LoginController.class, "loginPage").forward("success").to("/pages/login.jsp")
	 * </code>
	 * 
	 * <p>
	 * You can use Regular Expression in path, and named groups in the
	 * expression is considered as a parameter candidate
	 * </p>
	 * 
	 * <p>
	 * <strong>For example:</strong>
	 * </p>
	 * With following configuration code:<br/>
	 * <code> serve("/user/(?<name>.*?)")... <code><br/>
	 * And following controller method:<br/>
	 * <code> public void example(String name){ <code><br/>
	 * When you acccess /user/somename, the <i>name</i> parameter will have
	 * value <i>somename</i>
	 * 
	 * @param path
	 *            The path to serve
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder servePattern(String path) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, true, config);
		configBuilders.add(builder);
		return builder;

	}

	/**
	 * This is beginning of a configuration entry, that serves a path in this
	 * application <br/>
	 * This is like <code>serve(String path)</code> but this one will serve only
	 * specified method, like GET, POST etc. A configuration entry looks like
	 * this: <br/>
	 * <code>
	 * 	serve("/login", "GET").with(LoginController.class, "loginPage").forward("success").to("/pages/login.jsp")
	 * </code>
	 * 
	 * <p>
	 * You can use a simple expression ${parameterName} in path, to make part of
	 * the URL a parameter candidate
	 * </p>
	 * 
	 * <p>
	 * You can use Regular Expression in path, and named groups in the
	 * expression is considered as a parameter candidate
	 * </p>
	 * 
	 * <p>
	 * <strong>For example:</strong>
	 * </p>
	 * With following configuration code:<br/>
	 * <code> serve("/user/(?<name>.*?)")... <code><br/>
	 * And following controller method:<br/>
	 * <code> public void example(String name){ <code><br/>
	 * When you acccess /user/somename, the <i>name</i> parameter will have
	 * value <i>somename</i>
	 * 
	 * @param path
	 *            The path to serve
	 * @param method
	 *            The method to serve (GET, POST, PUT etc.)
	 * @return a ConfigBuilder for further configuration
	 */
	protected ConfigBuilder servePattern(String path, String method) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, true, method, config);
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
	 * </code> will make all 3 serves handles NotAuthorizedException with
	 * NotAuthorizedHandler <br/>
	 * It's even better in Java 8 with lambda:<code><pre>
	 * all(() -> {
	 * 	serve("/api/user/all").with(APIController.class, "list");
	 * 	serve("/api/user/add", "POST").with(APIController.class, "add");
	 * 	serve("/api/user/{name}", "GET").with(APIController.class, "get");
	 * }).handle(NotAuthorizedException.class, new NotAuthorizedHandler());
	 * </pre></code>
	 * 
	 * @param r
	 *            code to run in
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
