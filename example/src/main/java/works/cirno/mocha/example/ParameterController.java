package works.cirno.mocha.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import works.cirno.mocha.result.View;

public class ParameterController {
	@Inject
	@Named("userService")
	private UserService userService;

	private JsonFactory jsonFactory = new JsonFactory();

	public String index() {
		// Direct return a view name to redirect / forward to configured view.
		return "success";
	}

	public View show(User user) {

		// Return view with attributes, attributes will put to
		// HttpServletRequest
		return new View("success")
				.attribute("user", user);
	}

	public View user(String userId) {
		User user = userService.getUser(userId);
		return new View("success")
				.attribute("userId", userId)
				.attribute("user", user);
	}

	public void userPost(final String userId, User user, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		userService.saveUser(userId, user);
		resp.sendRedirect(req.getContextPath() + "/parameter/+" + userId + ".json");

	}

	public Object userJson(String userId, PrintWriter out) throws IOException {
		User user = userService.getUser(userId);
		if (user != null) {
			try (JsonGenerator gen = jsonFactory.createGenerator(out)) {
				gen.writeStartObject();
				{
					gen.writeStringField("title", user.getTitle());
					gen.writeStringField("firstName", user.getFirstName());
					gen.writeStringField("lastName", user.getLastName());
					gen.writeStringField("tel", user.getTel());
					gen.writeObjectField("income", user.getIncome());
				}
				gen.writeEndObject();
			}
			return null;
		} else {
			return 404;
		}
	}
}
