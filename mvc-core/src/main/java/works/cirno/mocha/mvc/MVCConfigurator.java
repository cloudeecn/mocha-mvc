package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.factory.BasicObjectFactory;
import works.cirno.mocha.mvc.factory.ObjectFactory;
import works.cirno.mocha.mvc.parameter.name.AnnotationParameterAnalyzer;
import works.cirno.mocha.mvc.parameter.name.ParameterAnalyzer;
import works.cirno.mocha.mvc.resolver.InvokeTargetResolver;
import works.cirno.mocha.mvc.resolver.RegexInvokeTargetResolver;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 */
public abstract class MVCConfigurator {

    protected static final String SUCCESS = "success";
    protected static final String FAILED = "failed";

    private final ArrayList<ConfigBuilderImpl> configBuilders = new ArrayList<>();
    private ObjectFactory objectFactory;
    private InvokeTargetResolver invokeTargetResolver;
    private ParameterAnalyzer parameterAnalyzer;

    protected void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    protected void setInvokeTargetResolver(InvokeTargetResolver invokeTargetResolver) {
        this.invokeTargetResolver = invokeTargetResolver;
    }

    protected void setParameterAnalyzer(ParameterAnalyzer parameterAnalyzer) {
        this.parameterAnalyzer = parameterAnalyzer;
    }

    protected ConfigBuilder serve(String path) {
        ConfigBuilderImpl builder = new ConfigBuilderImpl(path);
        configBuilders.add(builder);
        return builder;
    }

    protected void include(MVCConfigurator configurator) {
        configurator.configure();
        configBuilders.addAll(configurator.configBuilders);
    }

    Iterable<ConfigBuilderImpl> getConfigBuilders() {
        return Collections.unmodifiableCollection(configBuilders);
    }

    ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    InvokeTargetResolver getInvokeTargetResolver() {
        return invokeTargetResolver;
    }

    ParameterAnalyzer getParameterAnalyzer() {
        return parameterAnalyzer;
    }

    void finishConfigure() {
        if (objectFactory == null) {
            objectFactory = new BasicObjectFactory();
        }

        if (invokeTargetResolver == null) {
            invokeTargetResolver = new RegexInvokeTargetResolver();
        }

        if (parameterAnalyzer == null) {
            parameterAnalyzer = new AnnotationParameterAnalyzer();
        }
    }

    public abstract void configure();
}
