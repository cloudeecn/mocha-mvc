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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import works.cirno.mocha.result.Renderer;
import works.cirno.mocha.result.ResultType;

class MVCConfig implements ConfigBuilder {

	private MVCConfig parent;

	private TypeOrInstance<?> controller;
	private String methodName;

	private File uploadTemp;
	private Boolean raw;
	private Map<Class<?>, TypeOrInstance<? extends Renderer>> handlers;
	private HashMap<Class<?>, TypeOrInstance<? extends Renderer>> handlersCustom = new HashMap<>();
	private List<TypeOrInstance<? extends Renderer>> resultRenderers;
	private List<TypeOrInstance<? extends Renderer>> resultRenderersAppend = new ArrayList<>();
	private List<TypeOrInstance<? extends Renderer>> resultRenderersPrepend = new LinkedList<>();

	private HashMap<String, ServletResultRendererConfigImpl> pendingServletResultRendererConfig = new HashMap<>();

	public MVCConfig() {
	}

	public MVCConfig(MVCConfig parent) {
		this.parent = parent;
	}

	MVCConfig getParent() {
		return parent;
	}

	TypeOrInstance<?> getController() {
		return controller == null ? parent == null ? null : parent.getController() : controller;
	}

	String getMethodName() {
		return methodName == null ? parent == null ? null : parent.getMethodName() : methodName;
	}

	Map<Class<?>, TypeOrInstance<? extends Renderer>> getHandlers() {
		if (handlers == null) {
			if (handlersCustom.size() == 0) {
				handlers = parent == null ? Collections.<Class<?>, TypeOrInstance<? extends Renderer>> emptyMap()
						: parent.getHandlers();
			} else {
				handlers = new HashMap<>();
				if (parent != null) {
					handlers.putAll(parent.getHandlers());
				} else {
					handlers.putAll(handlersCustom);
				}
			}
		}
		return handlers;
	}

	List<TypeOrInstance<? extends Renderer>> getResultRenderers() {
		if (resultRenderers == null) {
			if (resultRenderersAppend.size() + resultRenderersPrepend.size() == 0) {
				resultRenderers = parent == null ? Collections.<TypeOrInstance<? extends Renderer>> emptyList()
						: parent.getResultRenderers();
			} else {
				resultRenderers = new ArrayList<>();
				resultRenderers.addAll(resultRenderersPrepend);
				if (parent != null) {
					resultRenderers.addAll(parent.getResultRenderers());
				}
				resultRenderers.addAll(resultRenderersAppend);
			}
		}
		return resultRenderers;
	}

	HashMap<String, ServletResultRendererConfigImpl> getPendingServletResultRendererConfig() {
		HashMap<String, ServletResultRendererConfigImpl> psrrConfig = new HashMap<>();
		if (parent != null) {
			psrrConfig.putAll(parent.getPendingServletResultRendererConfig());
		}
		psrrConfig.putAll(pendingServletResultRendererConfig);
		return psrrConfig;
	}

	boolean isRaw() {
		return raw == null ? (parent == null ? false : parent.isRaw()) : raw;
	}

	File getUploadTemp() {
		return uploadTemp == null
				? parent == null ? new File(System.getProperty("java.io.tmpdir")) : parent.getUploadTemp() : uploadTemp;
	}

	@Override
	public ConfigBuilder with(Class<?> controller, String methodName) {
		with(controller);
		withMethod(methodName);
		return this;
	}

	@Override
	public ConfigBuilder with(String controllerName, String methodName) {
		with(controllerName);
		withMethod(methodName);
		return this;
	}

	@Override
	public ConfigBuilder with(String controllerName) {
		this.controller = new TypeOrInstance<>(controllerName, Object.class);
		return this;
	}

	@Override
	public ConfigBuilder with(Class<?> controller) {
		this.controller = new TypeOrInstance<>(controller);
		return this;
	}

	@Override
	public ConfigBuilder withMethod(String methodName) {
		this.methodName = methodName;
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, Renderer result) {
		this.handlers.put(exception, new TypeOrInstance<>(result));
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception,
			Class<? extends Renderer> resultType) {
		this.handlers.put(exception, new TypeOrInstance<>(resultType));
		return this;
	}

	@Override
	public <T extends Throwable> ConfigBuilder exception(Class<T> exception, String resultName) {
		this.handlers.put(exception, new TypeOrInstance<>(resultName, Renderer.class));
		return this;
	}

	@Override
	public ServletResultRendererConfig forward(String resultName) {
		ServletResultRendererConfigImpl srrConfig = new ServletResultRendererConfigImpl(this, ResultType.forward);
		pendingServletResultRendererConfig.put(resultName, srrConfig);
		return srrConfig;
	}

	@Override
	public ServletResultRendererConfig redirect(String resultName) {
		ServletResultRendererConfigImpl srrConfig = new ServletResultRendererConfigImpl(this, ResultType.redirect);
		pendingServletResultRendererConfig.put(resultName, srrConfig);
		return srrConfig;
	}

	@Override
	public ConfigBuilder prependResultRenderer(Renderer renderer) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder prependResultRenderer(Class<? extends Renderer> renderer) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder prependResultRenderer(String rendererName) {
		resultRenderersPrepend.add(0, new TypeOrInstance<>(rendererName, Renderer.class));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(Renderer renderer) {
		resultRenderersAppend.add(new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(Class<? extends Renderer> renderer) {
		resultRenderersAppend.add(new TypeOrInstance<>(renderer));
		return this;
	}

	@Override
	public ConfigBuilder appendResultRenderer(String rendererName) {
		resultRenderersAppend.add(new TypeOrInstance<>(rendererName, Renderer.class));
		return this;
	}

	@Override
	public ConfigBuilder raw() {
		this.raw = true;
		return this;
	}

	@Override
	public ConfigBuilder uploadTemp(File tempDirectory) {
		this.uploadTemp = tempDirectory;
		return this;
	}
}
