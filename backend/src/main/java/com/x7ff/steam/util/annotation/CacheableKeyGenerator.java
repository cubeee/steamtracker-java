package com.x7ff.steam.util.annotation;

import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.annotation.AnnotationUtils;

public final class CacheableKeyGenerator implements KeyGenerator {
	public static final String NAME = "cacheableKeyGenerator";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		CacheableKey annotation = AnnotationUtils.findAnnotation(method, CacheableKey.class);
		Preconditions.checkNotNull(annotation, "A CacheableKey annotation is required when using a CacheableKeyGenerator");
		return annotation.value();
	}

}