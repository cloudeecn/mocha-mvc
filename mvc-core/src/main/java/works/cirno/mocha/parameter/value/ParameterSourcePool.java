package works.cirno.mocha.parameter.value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import works.cirno.mocha.OrderedPair;

public class ParameterSourcePool {

	private static final ParameterSource<?>[] NO_RESOURCE = new ParameterSource[0];
	private static final ConcurrentHashMap<OrderedPair<Class<?>, Class<?>>, Boolean> ASSIGNABLE_CACHE = new ConcurrentHashMap<>();
	private ArrayList<ParameterSource<?>> sourceList;
	private ConcurrentHashMap<Class<?>, ParameterSource<?>[]> cache = new ConcurrentHashMap<>();

	public ParameterSourcePool(List<ParameterSource<?>> parameterSources) {
		sourceList = new ArrayList<>(parameterSources);
	}

	@SuppressWarnings("unchecked")
	public <T> ParameterSource<T>[] getParameterSourcesFor(Class<T> type) {

		ParameterSource<T>[] ret = (ParameterSource<T>[])cache.get(type);
		if (ret == null) {
			ArrayList<ParameterSource<?>> list = new ArrayList<>(sourceList.size());
			for (ParameterSource<?> source : sourceList) {
				Class<?> sourceType = source.supportsType();
				OrderedPair<Class<?>, Class<?>> pair = new OrderedPair<Class<?>, Class<?>>(type, sourceType);
				Boolean assignable = ASSIGNABLE_CACHE.get(pair);
				if (assignable == null) {
					assignable = type.isAssignableFrom(sourceType);
					ASSIGNABLE_CACHE.putIfAbsent(pair, assignable);
				}
				if (Boolean.TRUE.equals(assignable)) {
					list.add(source);
				}
			}
			ret = list.size() == 0 ? NO_RESOURCE : list.toArray(new ParameterSource[list.size()]);
			cache.putIfAbsent(type, ret);
		}

		return ret;
	}
}
