package works.cirno.mocha.parameter.value;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeContext;
import works.cirno.mocha.parameter.name.Parameter;

public class CombianParameterSource implements ParameterSource {
	
	private static final Logger log = LoggerFactory.getLogger(CombianParameterSource.class);

	private Collection<ParameterSource> sources;

	public CombianParameterSource() {

	}

	public CombianParameterSource(Collection<ParameterSource> sources) {
		this.sources = sources;
	}

	public Collection<ParameterSource> getSources() {
		return sources;
	}

	public void setSources(Collection<ParameterSource> sources) {
		this.sources = sources;
	}

	@Override
	public Object getParameterValue(InvokeContext ctx, Parameter parameter) {
		for (ParameterSource source : sources) {
			Object value = source.getParameterValue(ctx, parameter);
			log.debug("Getting parameter {}, result {}", parameter, value);
			if (value != NOT_HERE) {
				return value;
			}
		}
		return NOT_HERE;
	}

}
