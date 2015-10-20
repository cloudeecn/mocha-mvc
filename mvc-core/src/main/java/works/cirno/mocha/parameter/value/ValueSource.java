package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public interface ValueSource {
	static final Object NOT_FOUND = new Object() {
		public String toString() {
			return "##NOT_FOUND";
		};
	};

	Object getParameter(InvokeContext ctx, String key);

	ConvertStrategy getConvertStrategy();
}
