package works.cirno.mocha.example.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import works.cirno.mocha.example.ProfileController;
import works.cirno.mocha.example.UserService;
import works.cirno.mocha.example.UserServiceImpl;

public class ExampleModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(UserService.class).annotatedWith(Names.named("userService")).to(UserServiceImpl.class).in(Singleton.class);

	}

}
