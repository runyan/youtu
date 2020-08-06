package com.maoxiong.youtu.util.configfile;

import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.impl.LruCache;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigFileUtil {

	static final LruCache<String, Properties> PROPERTY_CACHE = new LruCache<>(64);
	
	static final String DEFAULT_PROPERTIES_CONFIG_FILE_PATH = "/youtu.properties";
	static final String DEFAULT_YAML_CONFIG_FILE_PATH = "youtu.yml";
	
	private static final String PROPERTIES = "properties";
	private static final String YML = "yml";
	private static final String YAML = "yaml";
	
	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Properties loadProperties(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return loadFromDefaultFilePath();
		}
		if (filePath.endsWith(PROPERTIES)) {
			return PropertyUtil.loadProperties(filePath);
		} else if(filePath.endsWith(YML) || filePath.endsWith(YAML)) {
			return YamlUtil.loadProperties(filePath);
		}
		throw new IllegalArgumentException("unsupportted file type");
	}
	
	private static Properties loadFromDefaultFilePath() {
		boolean isEmpty = false;
		Properties props = PropertyUtil.loadProperties(DEFAULT_PROPERTIES_CONFIG_FILE_PATH);
		if (Objects.isNull(props)) {
			isEmpty = true;
		}
		Properties yamlProperties = YamlUtil.loadProperties(DEFAULT_YAML_CONFIG_FILE_PATH);
		if (Objects.isNull(yamlProperties)) {
			if (!isEmpty) {
				return props;
			}
			throw new RuntimeException("no config file path provided, nor default config files provided");
		}
		return yamlProperties;
	}
	
}
