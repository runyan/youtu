package com.maoxiong.youtu.annotation;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.util.configfile.ConfigFileUtil;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigAnnotationParser {

	public static Properties parseConfigProperties(Class<?> configClass) {
		if (!configClass.isAnnotationPresent(YoutuConfig.class)) {
			throw new IllegalStateException("annotaion: " + YoutuConfig.class.getSimpleName() + 
					" is not present for class: " + configClass.getName());
		}
		YoutuConfig config = configClass.getAnnotation(YoutuConfig.class);
		String configFilePath = config.congfigFilePath();
		if (!StringUtils.isEmpty(configFilePath)) {
			return ConfigFileUtil.loadProperties(configFilePath);
		}
		Properties props = new Properties();
		props.setProperty("youtu.qq", config.qq());
		props.setProperty("youtu.appId", config.appId());
		props.setProperty("youtu.secretId", config.secretId());
		props.setProperty("youtu.secretKey", config.secretKey());
		props.setProperty("youtu.fileSavePath", config.fileSavePath());
		return props;
	}
}
