package works.cirno.mocha.mvc.parameter.name;

import org.junit.Assert;
import org.junit.Test;
import works.cirno.mocha.mvc.InvokeTarget;

/**
 *
 */
public class Java8ParameterAnalyzerTest {

    private ThreadLocal<Java8ParameterAnalyzer> analyzer = new ThreadLocal<Java8ParameterAnalyzer>(){
        @Override
        protected Java8ParameterAnalyzer initialValue() {
            return new Java8ParameterAnalyzer();
        }
    };

    @Test
    public void testNamedArguments1() throws Exception{
        Java8ParameterAnalyzer analyzer = this.analyzer.get();
        Class<?> controllerClass = TestController.class;
        Object controller = controllerClass.newInstance();

        InvokeTarget target = new InvokeTarget(null, controllerClass, "method1", controller);
        Parameter[] parameters = analyzer.getParameters(target);
        Assert.assertEquals(0, parameters.length);
    }

    @Test
    public void testNamedArguments2() throws Exception{
        Java8ParameterAnalyzer analyzer = this.analyzer.get();
        Class<?> controllerClass = TestController.class;
        Object controller = controllerClass.newInstance();

        InvokeTarget target = new InvokeTarget(null, controllerClass, "method2", controller);
        Parameter[] parameters = analyzer.getParameters(target);

        Assert.assertEquals(3, parameters.length);
        Assert.assertEquals("name", parameters[0].getName());
        Assert.assertEquals("id", parameters[1].getName());
        Assert.assertEquals("uuid", parameters[2].getName());
    }

    @Test
    public void testNamedArguments3() throws Exception{
        Java8ParameterAnalyzer analyzer = this.analyzer.get();
        Class<?> controllerClass = TestController.class;
        Object controller = controllerClass.newInstance();

        InvokeTarget target = new InvokeTarget(null, controllerClass, "method3", controller);
        Parameter[] parameters = analyzer.getParameters(target);

        Assert.assertEquals(3, parameters.length);
        Assert.assertEquals("name", parameters[0].getName());
        Assert.assertEquals("id", parameters[1].getName());
        Assert.assertEquals("uuid", parameters[2].getName());
    }

    @Test
    public void testNamedArguments4() throws Exception{
        Java8ParameterAnalyzer analyzer = this.analyzer.get();
        Class<?> controllerClass = TestController.class;
        Object controller = controllerClass.newInstance();

        InvokeTarget target = new InvokeTarget(null, controllerClass, "method4", controller);
        Parameter[] parameters = analyzer.getParameters(target);

        Assert.assertEquals(3, parameters.length);
        Assert.assertEquals("name", parameters[0].getName());
        Assert.assertEquals("id", parameters[1].getName());
        Assert.assertEquals("uuid", parameters[2].getName());
    }

}
