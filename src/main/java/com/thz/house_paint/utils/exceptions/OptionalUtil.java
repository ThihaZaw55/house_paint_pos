package com.thz.house_paint.utils.exceptions;

import java.util.Optional;
import java.util.function.Function;

public class OptionalUtil {

	public static<T, V> Function<String, Function<String, Function<V, T>>> safeCall(Optional<T> optional) {
		return resource -> type -> value -> optional.orElseThrow(() -> new BusinessRuleViolationException("There is no %s with %s %s.".formatted(resource, type, value)));
	}
	
}
