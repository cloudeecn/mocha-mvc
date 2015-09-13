package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.parameter.name.TestController;

/**
 *
 */
public class MockInvokeTarget extends InvokeTarget {

    private String testData;

    public MockInvokeTarget(String testData) {
        super(null, TestController.class, "method1", new TestController());
        this.testData = testData;
    }

    public String getTestData() {
        return testData;
    }
}
