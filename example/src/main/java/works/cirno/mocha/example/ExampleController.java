package works.cirno.mocha.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import works.cirno.mocha.parameter.name.NamedParam;
import works.cirno.mocha.result.View;

/**
 *
 */
public class ExampleController {

	public View hello(@NamedParam("name") String name) {
		if (name == null) {
			name = "world";
		}
		return new View("success")
				.attribute("name", name)
				.attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	public View hello2(@NamedParam("name") String name) {
		if (name == null) {
			name = "world";
		}
		return new View("success")
				.attribute("name", name)
				.attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	public View hello3(@NamedParam("user") TitledUser user) {
		return new View("success")
				.attribute("user", user)
				.attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}
}
