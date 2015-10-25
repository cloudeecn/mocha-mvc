/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2015 Cloudee Huang ( https://github.com/cloudeecn / cloudeecn@gmail.com )
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
