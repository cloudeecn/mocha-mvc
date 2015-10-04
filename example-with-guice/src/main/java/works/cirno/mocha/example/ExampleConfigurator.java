package works.cirno.mocha.example;

import javax.inject.Named;
import javax.inject.Singleton;

import works.cirno.mocha.MVCConfigurator;

/**
 *
 */
@Named
@Singleton
public class ExampleConfigurator extends MVCConfigurator {

	@Override
	public void configure() {
		serve("/basic/hello/(?<name>.*?)").with(ExampleController.class, "hello").forward("success")
				.to("/jsp/hello.jsp").redirect("failed").to("/error");

		serve("/basic/hello2").with(ExampleController.class, "hello2").forward("success").to("/jsp/hello2.jsp");

		serve("/basic/hello3").with(ExampleController.class, "hello3").forward("success").to("/jsp/hello3.jsp");
	}
}
