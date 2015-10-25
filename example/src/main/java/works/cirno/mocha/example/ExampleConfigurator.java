package works.cirno.mocha.example;

import works.cirno.mocha.MVCConfigurator;

/**
 *
 */
public class ExampleConfigurator extends MVCConfigurator {

	@Override
	public void configure() {

		// Extract common parameters
		all(new Runnable() {

			@Override
			public void run() {
				serve("/parameter").withMethod("index").forward("success").to("/WEB-INF/jsp/parameter.jsp");
				serve("/parameter/show")
						.withMethod("show")
						.forward("success").to("/WEB-INF/jsp/parameter-show.jsp");
			}
		}).with(ParameterController.class);

		// Restful styled APIs with simple expression
		serve("/parameter/+${userId}").with(ParameterController.class, "user")
				.forward("success").to("/WEB-INF/jsp/parameter-rest.jsp");

		serve("/parameter/+${userId}", "POST").with(ParameterController.class, "userPost");

		serve("/parameter/+${userId}.json")
				.with(ParameterController.class, "userJson");

	}
}
