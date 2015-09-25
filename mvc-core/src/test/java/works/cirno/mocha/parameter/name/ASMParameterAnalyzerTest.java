package works.cirno.mocha.parameter.name;

import org.junit.Assert;
import org.junit.Test;

import works.cirno.mocha.InvokeTarget;
import works.cirno.mocha.parameter.name.ASMParameterAnalyzer;
import works.cirno.mocha.parameter.name.Parameter;

/**
 *
 */
public class ASMParameterAnalyzerTest {

	private ThreadLocal<ASMParameterAnalyzer> analyzer = new ThreadLocal<ASMParameterAnalyzer>() {
		@Override
		protected ASMParameterAnalyzer initialValue() {
			return new ASMParameterAnalyzer();
		}
	};

	@Test
	public void testNamedArguments1() throws Exception {
		ASMParameterAnalyzer analyzer = this.analyzer.get();
		Class<?> controllerClass = ASMTestController.class;
		Object controller = controllerClass.newInstance();

		InvokeTarget target = new InvokeTarget(null, controller, "method1");
		Parameter[] parameters = analyzer.getParameters(target);
		Assert.assertEquals(0, parameters.length);
	}

	@Test
	public void testNamedArguments2() throws Exception {
		ASMParameterAnalyzer analyzer = this.analyzer.get();
		Class<?> controllerClass = ASMTestController.class;
		Object controller = controllerClass.newInstance();

		InvokeTarget target = new InvokeTarget(null, controller, "method2");
		Parameter[] parameters = analyzer.getParameters(target);

		Assert.assertEquals(3, parameters.length);
		Assert.assertEquals("name", parameters[0].getName());
		Assert.assertEquals("id", parameters[1].getName());
		Assert.assertEquals("uuid", parameters[2].getName());
	}

	@Test
	public void testNamedArguments3() throws Exception {
		ASMParameterAnalyzer analyzer = this.analyzer.get();
		Class<?> controllerClass = ASMTestController.class;
		Object controller = controllerClass.newInstance();

		InvokeTarget target = new InvokeTarget(null, controller, "method3");
		Parameter[] parameters = analyzer.getParameters(target);

		Assert.assertEquals(3, parameters.length);
		Assert.assertEquals("name", parameters[0].getName());
		Assert.assertEquals("id", parameters[1].getName());
		Assert.assertEquals("uuid", parameters[2].getName());
	}

	@Test
	public void testNamedArguments4() throws Exception {
		ASMParameterAnalyzer analyzer = this.analyzer.get();
		Class<?> controllerClass = ASMTestController.class;
		Object controller = controllerClass.newInstance();

		InvokeTarget target = new InvokeTarget(null, controller, "method4");
		Parameter[] parameters = analyzer.getParameters(target);

		Assert.assertEquals(3, parameters.length);
		Assert.assertEquals("name", parameters[0].getName());
		Assert.assertEquals("id", parameters[1].getName());
		Assert.assertEquals("uuid", parameters[2].getName());
	}

}
