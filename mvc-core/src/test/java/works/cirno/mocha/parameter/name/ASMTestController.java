package works.cirno.mocha.parameter.name;

/**
 *
 */
public class ASMTestController {
    public void methodD() {

    }

    public void methodD(String arg) {

    }

    public void method1() {

    }

    public String method2(String name, Integer id, String uuid) {
        return name + id;
    }

    public String method3(String name, Integer id, String uuid) {
        {
            Integer j = name.hashCode() + id + uuid.hashCode();
            id = j;
        }

        Integer i = 15;
        return i + "a";
    }

    public String method4(String name, Integer id, String uuid) {
        return 1 + uuid;
    }
}
