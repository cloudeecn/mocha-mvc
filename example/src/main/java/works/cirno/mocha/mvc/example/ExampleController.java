package works.cirno.mocha.mvc.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import works.cirno.mocha.mvc.Forward;
import works.cirno.mocha.mvc.Result;
import works.cirno.mocha.mvc.parameter.name.NamedParam;

/**
 *
 */
public class ExampleController {

	public Result hello(@NamedParam("name") String name) {
		if (name == null) {
            name = "world";
		}
		return new Forward("/jsp/hello.jsp")
                .attribute("name", name)
                .attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

    public Result hello2(@NamedParam("name") String name) {
        if (name == null) {
            name = "world";
        }
        return new Forward("/jsp/hello2.jsp")
                .attribute("name", name)
                .attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public Result hello3(@NamedParam("user") TitledUser user) {
        return new Forward("/jsp/hello3.jsp")
                .attribute("user", user)
                .attribute("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}
