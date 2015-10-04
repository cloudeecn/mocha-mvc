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

		all(new Runnable() {

			@Override
			public void run() {
				serve("/profile/${name}")
						.withMethod("index")
						.forward("success").to("/WEB-INF/jsp/profile.jsp");

				serve("/profile/${name}/photo", "GET")
						.withMethod("photo");

				serve("/profile/${name}/photo", "POST")
						.withMethod("upload")
						.rawEntity();
			}
		}).with(ProfileController.class);

		serve("/parameter")
				.with(ParameterController.class, "index")
				.forward("success").to("/WEB-INF/jsp/parameter.jsp");

		serve("/parameter/show")
				.with(ParameterController.class, "show")
				.forward("success").to("/WEB-INF/jsp/parameter-show.jsp");
	}
}
