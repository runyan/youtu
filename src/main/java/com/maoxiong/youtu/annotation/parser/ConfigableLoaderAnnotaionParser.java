package com.maoxiong.youtu.annotation.parser;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;

public class ConfigableLoaderAnnotaionParser {

	public static Integer getConfigFilePriority(Class<?> clz) {
		if (!clz.isAnnotationPresent(ConfigLoaderConfiguration.class)) {
			return Integer.MIN_VALUE;
		}
		ConfigLoaderConfiguration config = clz.getAnnotation(ConfigLoaderConfiguration.class);
		return config.priority();
	}
	
	public static String getConfigLoaderSuffix(Class<?> clz) {
		if (!clz.isAnnotationPresent(ConfigLoaderConfiguration.class)) {
			return StringUtils.EMPTY;
		}
		ConfigLoaderConfiguration config = clz.getAnnotation(ConfigLoaderConfiguration.class);
		return config.suffix();
	}
}
