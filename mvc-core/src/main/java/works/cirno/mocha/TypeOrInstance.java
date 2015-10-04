package works.cirno.mocha;

public class TypeOrInstance<T> {

	public static enum InjectBy {
		NAME_TYPE, TYPE, INSTANCE
	}

	private InjectBy injectBy;
	private Class<? extends T> type;
	private T instance;
	private String name;

	public TypeOrInstance(String name, Class<? extends T> type) {
		this.injectBy = InjectBy.NAME_TYPE;
		this.name = name;
		this.type = type;
	}

	public TypeOrInstance(T instance) {
		this.injectBy = InjectBy.INSTANCE;
		this.instance = instance;
	}

	public TypeOrInstance(Class<? extends T> type) {
		this.injectBy = InjectBy.TYPE;
		this.type = type;
	}

	public InjectBy getInjectBy() {
		return injectBy;
	}

	public Class<? extends T> getType() {
		return type;
	}

	public T getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

}
