package works.cirno.mocha.mvc.example;

import works.cirno.mocha.mvc.MVCConfigurator;

/**
 *
 */
public class ExampleConfigurator extends MVCConfigurator {
    @Override
    public void configure() {
        serve("/hello/(?<name>.*?)").with(ExampleController.class, "hello");
        serve("/hello2").with(ExampleController.class, "hello2");
        serve("/hello3").with(ExampleController.class, "hello3");
    }
}
