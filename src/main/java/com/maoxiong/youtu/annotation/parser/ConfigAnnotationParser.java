package com.maoxiong.youtu.annotation.parser;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.YoutuConfig;
import com.maoxiong.youtu.constants.ConfigConstans;
import com.maoxiong.youtu.util.ConfigFileUtil;

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
		if (StringUtils.isNotEmpty(configFilePath)) {
			return ConfigFileUtil.loadProperties(configFilePath);
		}
		Properties props = new Properties();
		props.setProperty(ConfigConstans.QQ, config.qq());
		props.setProperty(ConfigConstans.APP_ID, config.appId());
		props.setProperty(ConfigConstans.SECRET_ID, config.secretId());
		props.setProperty(ConfigConstans.SECRET_KEY, config.secretKey());
		props.setProperty(ConfigConstans.FILE_SAVE_PATH, config.fileSavePath());
		return props;
	}
}
