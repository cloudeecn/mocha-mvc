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

	protected ConfigBuilder serve(String path) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, config);
		configBuilders.add(builder);
		return builder;
	}

	protected ConfigBuilder serve(String path, String method) {
		ConfigBuilderImpl builder = new ConfigBuilderImpl(path, method, config);
		configBuilders.add(builder);
		return builder;
	}

	protected void include(MVCConfigurator configurator) {
		configurator.configure();
		configBuilders.addAll(configurator.configBuilders);
	}

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
