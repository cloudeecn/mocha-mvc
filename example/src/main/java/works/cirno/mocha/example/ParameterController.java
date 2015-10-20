package works.cirno.mocha.example;

import javax.inject.Inject;
import javax.inject.Named;

import works.cirno.mocha.result.View;

public class ParameterController {
	@Inject
	@Named("userService")
	private UserService userService;

	public String index() {
		// Direct return a view name to redirect / forward to configured view.
		return "success";
	}

	public View show(TitledUser user1, TitledUser user2, boolean hasUser2) {

		// Return view with attributes, attributes will put to
		// HttpServletRequest
		return new View("success")
				.attribute("user1", user1)
				.attribute("user1Hash", userService.getUserHash(user1))
				.attribute("user2", hasUser2 ? user2 : null)
				.attribute("user2Hash", userService.getUserHash(user2));
	}

}
