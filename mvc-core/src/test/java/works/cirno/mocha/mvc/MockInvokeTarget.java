package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.parameter.name.ASMTestController;

/**
 *
 */
public class MockInvokeTarget extends InvokeTarget {

    private String testData;

    public MockInvokeTarget(String testData) {
        super(null, ASMTestController.class, "method1", new ASMTestController());
        this.testData = testData;
    }

    public String getTestData() {
        return testData;
    }
}
