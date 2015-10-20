package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public interface ParameterSource {
	static final Object NOT_HERE = new Object(){
		public String toString() {
			return "NOT_HERE";
		};
	};

	Object getParameterValue(InvokeContext ctx, Parameter parameter);
}
