package works.cirno.mocha.mvc.example;

import works.cirno.mocha.mvc.MVCConfigurator;
import works.cirno.mocha.mvc.parameter.name.ASMParameterAnalyzer;
import works.cirno.mocha.mvc.parameter.name.JDK8ParameterAnalyzer;

/**
 *
 */
public class ExampleConfigurator extends MVCConfigurator {
	@Override
	public void configure() {
		serve("/basic/hello/(?<name>.*?)").with(ExampleController.class, "hello").forward("success")
				.to("/jsp/hello.jsp");

		serve("/basic/hello2").with(ExampleController.class, "hello2").forward("success").to("/jsp/hello2.jsp");

		serve("/basic/hello3").with(ExampleController.class, "hello3").forward("success").to("/jsp/hello3.jsp");

		/*
		 * A group of serves can be configured with same configuration with
		 * "all" method. Following serves will get parameter name with ASM.
		 */
		all(new Runnable() {

			@Override
			public void run() {
				serve("/asm/hello/(?<name>.*?)").with(ExampleController.class, "hello").forward("success")
						.to("/jsp/hello.jsp");

				serve("/asm/hello2").with(ExampleController.class, "hello2").forward("success").to("/jsp/hello2.jsp");

				serve("/asm/hello3").with(ExampleController.class, "hello3").forward("success").to("/jsp/hello3.jsp");
			}
		}).parameterNamedBy(new ASMParameterAnalyzer());

		/*
		 * It's elegance to use lambda too. And with JDK8, it can get parameter
		 * name with Java8 parameters. Please add -parameters into build
		 * parameter.
		 */
		all(() -> {
			serve("/jdk8/hello/(?<name>.*?)").with(ExampleController.class, "hello").forward("success")
					.to("/jsp/hello.jsp");

			serve("/jdk8/hello2").with(ExampleController.class, "hello2").forward("success").to("/jsp/hello2.jsp");

			serve("/jdk8/hello3").with(ExampleController.class, "hello3").forward("success").to("/jsp/hello3.jsp");
		}).parameterNamedBy(new JDK8ParameterAnalyzer());

	}
}
