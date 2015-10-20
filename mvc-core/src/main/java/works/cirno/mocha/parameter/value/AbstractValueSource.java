package works.cirno.mocha.parameter.value;

public abstract class AbstractValueSource implements ValueSource {

	private ConvertStrategy strategy = ConvertStrategy.NULL_WHEN_UNCONVERTABLE;

	ValueSource withConvertStrategy(ConvertStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	@Override
	public ConvertStrategy getConvertStrategy() {
		return strategy;
	}

}
