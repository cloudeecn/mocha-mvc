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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.cirno.mocha.result.Renderer;

/**
 *
 */
class ConfigBuilderImpl implements ConfigBuilder {

	private String path;
	private String method;
	private boolean pathRegex;

	private MVCConfig config;

	ConfigBuilderImpl(String path, boolean pathRegex, MVCConfig parent) {
		this(path, pathRegex, null, parent);
	}

	ConfigBuilderImpl(String path, boolean pathRegex, String method, MVCConfig parent) {
		this.path = path;
		this.method = method;
		this.config = new MVCConfig(parent);
	}

	String getPath() {
		return path;
	}

	boolean isPathRegex() {
		return pathRegex;
	}

	String getMethod() {
		return method;
	}

	TypeOrInstance<?> getController() {
		return config.getController();
	}

	String getMethodName() {
		return config.getMethodName();
	}

	Map<Class<?>, TypeOrInstance<? extends Renderer>> getExceptionHandlers() {
		return config.getHandlers();
	}

	List<TypeOrInstance<? extends Renderer>> getResultRenderers() {
		return config.getResultRenderers();
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		return config.getPendingServletResultRendererConfig();
	}

	boolean isRaw() {
		return config.isRaw();
	}

	File getUploadTemp() {
		return config.getUploadTemp();
	}

	public ConfigBuilder with(Class<?> controller, String methodName) {
		return config.with(controller, methodName);
	}

	public ConfigBuilder with(String controllerName, String methodName) {
		return config.with(controllerName, methodName);
	}

	public ConfigBuilder with(String controllerName) {
		return config.with(controllerName);
	}

	public ConfigBuilder with(Class<?> controller) {
		return config.with(controller);
	}

	public ConfigBuilder withMethod(String methodName) {
		return config.withMethod(methodName);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, Renderer result) {
		return config.exception(exception, result);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception,
			Class<? extends Renderer> resultType) {
		return config.exception(exception, resultType);
	}

	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, String resultName) {
		return config.exception(exception, resultName);
	}

	public ServletResultRendererConfig forward(String resultName) {
		return config.forward(resultName);
	}

	public ServletResultRendererConfig redirect(String resultName) {
		return config.redirect(resultName);
	}

	public ConfigBuilder prependResultRenderer(Renderer renderer) {
		return config.prependResultRenderer(renderer);
	}

	public ConfigBuilder prependResultRenderer(Class<? extends Renderer> renderer) {
		return config.prependResultRenderer(renderer);
	}

	public ConfigBuilder prependResultRenderer(String rendererName) {
		return config.prependResultRenderer(rendererName);
	}

	public ConfigBuilder appendResultRenderer(Renderer renderer) {
		return config.appendResultRenderer(renderer);
	}

	public ConfigBuilder appendResultRenderer(Class<? extends Renderer> renderer) {
		return config.appendResultRenderer(renderer);
	}

	public ConfigBuilder appendResultRenderer(String rendererName) {
		return config.appendResultRenderer(rendererName);
	}

	public ConfigBuilder raw() {
		return config.raw();
	}

	public ConfigBuilder uploadTemp(File tempDirectory) {
		return config.uploadTemp(tempDirectory);
	}

}
