package com.maoxiong.youtu.annotation.parser;

import java.util.Objects;

import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;
import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigableLoaderAnnotaionParser {
	
	private static final Cache<Class<?>, ConfigLoaderConfiguration> CACHE = 
			new CaffeineCache<>();

	public static Integer getConfigFilePriority(Class<?> clz) {
		ConfigLoaderConfiguration config = getAnnotation(clz);
		return config.priority();
	}
	
	public static String[] getConfigLoaderSuffixs(Class<?> clz) {
		ConfigLoaderConfiguration config = getAnnotation(clz);
		return config.suffixs();
	}
	
	public static String getDefaultFilePath(Class<?> clz) {
		ConfigLoaderConfiguration config = getAnnotation(clz);
		return config.defaultFilePath();
	}
	
	private static ConfigLoaderConfiguration getAnnotation(Class<?> clz) {
		ConfigLoaderConfiguration config = CACHE.getIfPresent(clz);
		if (Objects.nonNull(config)) {
			return config;
		} else {
			if (!clz.isAnnotationPresent(ConfigLoaderConfiguration.class)) {
				throw new IllegalStateException("annotaion: " + ConfigLoaderConfiguration.class.getSimpleName() + 
						" is not present for class: " + clz.getName());
			}
			config = clz.getAnnotation(ConfigLoaderConfiguration.class);
			CACHE.set(clz, config);
			return config;
		}
	}
}
