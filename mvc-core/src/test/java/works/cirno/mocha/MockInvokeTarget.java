package works.cirno.mocha;

import works.cirno.mocha.InvokeTarget;
import works.cirno.mocha.parameter.name.ASMTestController;

/**
 *
 */
public class MockInvokeTarget extends InvokeTarget {

	private String testData;

	public MockInvokeTarget(String testData) {
		super(null, new ASMTestController(), "method1");
		this.testData = testData;
	}

	public String getTestData() {
		return testData;
	}
}
