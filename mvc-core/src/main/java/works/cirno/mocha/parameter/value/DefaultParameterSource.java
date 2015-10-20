package works.cirno.mocha.parameter.value;

import java.util.ArrayList;

public class DefaultParameterSource extends CombianParameterSource {

	public static final DefaultParameterSource INSTANCE = new DefaultParameterSource();

	public DefaultParameterSource() {

		ArrayList<ParameterSource> sources = new ArrayList<>();

		sources.add(new TypedParameterSource());
		sources.add(new RawParameterSource());
		sources.add(new FilePartArrayParameterSource());
		sources.add(new FilePartParameterSource());

		MatrixParameterSource matrix = new MatrixParameterSource();
		ArrayList<ValueSource> vs = new ArrayList<>();
		vs.add(new MatcherValueSource().withConvertStrategy(ConvertStrategy.NULL_WHEN_UNCONVERTABLE));
		vs.add(new MultiPartStringArraySource().withConvertStrategy(ConvertStrategy.SKIP_WHEN_UNCONVERTABLE));
		vs.add(new MultiPartStringSource().withConvertStrategy(ConvertStrategy.NULL_WHEN_UNCONVERTABLE));
		vs.add(new RequestValueSource().withConvertStrategy(ConvertStrategy.SKIP_WHEN_UNCONVERTABLE));
		vs.add(new RequestArrayValueSource().withConvertStrategy(ConvertStrategy.NULL_WHEN_UNCONVERTABLE));
		matrix.setSources(vs);
		ArrayList<ValueConverter> vc = new ArrayList<>();
		vc.add(new StringPEConverter());
		vc.add(new StringEnumConverter());
		matrix.setConverters(vc);
		sources.add(matrix);

		BeanParameterSource beanParameterSource = new BeanParameterSource();
		beanParameterSource.setPropertySource(this);
		sources.add(beanParameterSource);

		this.setSources(sources);
	}

}
