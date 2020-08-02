package com.maoxiong.youtu.util;

import java.util.Objects;

/**
 * 
 * @author yanrun
 *
 */
public class CacheKeyUtil {
	
	private CacheKeyUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static String generateCacheKey(Object... params) {
		return String.valueOf(Objects.hash(params));
	}
}
