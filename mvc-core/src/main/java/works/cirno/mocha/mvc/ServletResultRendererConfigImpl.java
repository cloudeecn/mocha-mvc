package works.cirno.mocha.mvc;

import works.cirno.mocha.mvc.result.ResultType;

public class ServletResultRendererConfigImpl implements ServletResultRendererConfig {

	private ConfigBuilder configBuilder;
	private ResultType resultType;
	private String path;

	public ServletResultRendererConfigImpl(ConfigBuilder configBuilder, ResultType resultType) {
		super();
		this.configBuilder = configBuilder;
		this.resultType = resultType;
	}

	@Override
	public ConfigBuilder to(String path) {
		this.path = path;
		return configBuilder;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public String getPath() {
		return path;
	}

}
