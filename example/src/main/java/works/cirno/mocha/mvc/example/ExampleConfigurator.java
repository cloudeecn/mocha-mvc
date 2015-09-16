package works.cirno.mocha.mvc.example;

import works.cirno.mocha.mvc.MVCConfigurator;

/**
 *
 */
public class ExampleConfigurator extends MVCConfigurator {
    @Override
    public void configure() {
        serve("/hello/(?<name>.*?)").with(ExampleController.class, "hello")
        	.forward("success").to("/jsp/hello.jsp");
        
        serve("/hello2").with(ExampleController.class, "hello2")
        	.forward("success").to("/jsp/hello2.jsp");
        
        serve("/hello3").with(ExampleController.class, "hello3")
        	.forward("success").to("/jsp/hello3.jsp");
    }
}
