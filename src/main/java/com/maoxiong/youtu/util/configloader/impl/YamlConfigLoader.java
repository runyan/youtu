package com.maoxiong.youtu.util.configloader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;
import com.maoxiong.youtu.util.LogUtil;
import com.maoxiong.youtu.util.configloader.ConfigFileLoader;

/**
 * 
 * @author yanrun
 *
 */
@ConfigLoaderConfiguration(priority = 1, suffix = "yml")
public class YamlConfigLoader implements ConfigFileLoader {
	
	@Override
	public Properties loadProperties(String yamlFilePath) {
		Yaml yml = new Yaml();
		Properties props = new Properties();
		try (InputStream inputStream = YamlConfigLoader.class
				  .getClassLoader()
				  .getResourceAsStream(yamlFilePath)) {
			Map<String, Object> propertyMap = yml.load(inputStream);
			if (Objects.isNull(propertyMap)) {
				throw new RuntimeException("no config in yaml file: " + yamlFilePath);
			}
			parseMap(propertyMap, StringUtils.EMPTY, props);
			PROPERTY_CACHE.set(yamlFilePath, props);
			return props;
		} catch (YAMLException | IOException e) {
			String msg = e.getMessage();
			if (StringUtils.contains(msg, "java.io.IOException: Stream closed")) {
				LogUtil.warn("yaml file: {} does not exists", yamlFilePath);
			} else {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void parseMap(Map<String, Object> map, String baseKey, Properties props) {
		map.forEach((key, value) -> {
			key = StringUtils.isEmpty(baseKey) ? key : baseKey + "." + key;
			if (value instanceof Map) {
				parseMap((Map<String, Object>) value, key, props);
			} else {
				props.setProperty(key, Objects.isNull(value) ? StringUtils.EMPTY 
						: String.valueOf(value));
			}
		});
	}
	
	@Override
	public String getDefaultFilePath() {
		return "youtu.yml";
	}
	
}
