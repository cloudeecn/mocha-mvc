package works.cirno.mocha.parameter.name;

import works.cirno.mocha.parameter.name.NamedParam;

/**
 *
 */
public class TestController {
    public void methodD() {

    }

    public void methodD(@NamedParam("arg") String arg) {

    }

    public void method1() {

    }

    public String method2(@NamedParam("name") String name, @NamedParam("id") Integer id, @NamedParam("uuid") String uuid) {
        return name + id;
    }

    public String method3(@NamedParam("name") String name, @NamedParam("id") Integer id, @NamedParam("uuid") String uuid) {
        {
            Integer j = name.hashCode() + id + uuid.hashCode();
            id = j;
        }

        Integer i = 15;
        return i + "a";
    }

    public String method4(@NamedParam("name") String name, @NamedParam("id") Integer id, @NamedParam("uuid") String uuid) {
        return 1 + uuid;
    }
}
