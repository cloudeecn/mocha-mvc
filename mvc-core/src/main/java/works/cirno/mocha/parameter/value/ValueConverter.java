package works.cirno.mocha.parameter.value;

import works.cirno.mocha.InvokeContext;

public interface ValueConverter {
	static final Object NOT_CONVERTABLE = new Object() {
		public String toString() {
			return "##NOT_CONVERTABLE";
		};
	};

	Object convert(Class<?> type, InvokeContext ctx, Object from);
}
