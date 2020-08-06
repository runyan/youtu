package com.maoxiong.youtu.util.configfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class YamlUtil {
	
	private YamlUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static Properties loadProperties(String yamlFilePath) {
		Yaml yml = new Yaml();
		String path = StringUtils.isBlank(yamlFilePath) ? 
				ConfigFileUtil.DEFAULT_YAML_CONFIG_FILE_PATH : yamlFilePath;
		Properties props = ConfigFileUtil.PROPERTY_CACHE.getIfPresent(yamlFilePath);
		if (Objects.isNull(props)) {
			props = new Properties();
			try (InputStream inputStream = YamlUtil.class
					  .getClassLoader()
					  .getResourceAsStream(path)) {
				Map<String, Object> propertyMap = yml.load(inputStream);
				if (Objects.isNull(propertyMap)) {
					return null;
				}
				parseMap(propertyMap, "", props);
				ConfigFileUtil.PROPERTY_CACHE.set(yamlFilePath, props);
				return props;
			} catch (YAMLException | IOException e) {
				LogUtil.error(e.getMessage());
				return null;
			}
		} else {
			return props;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void parseMap(Map<String, Object> map, String baseKey, Properties props) {
		String key;
		Object value;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			key = entry.getKey();
			key = StringUtils.isEmpty(baseKey) ? key : baseKey + "." + key;
			value = entry.getValue();
			if (value instanceof Map) {
				parseMap((Map<String, Object>) value, key, props);
			} else {
				props.setProperty(key, Objects.isNull(value) ? StringUtils.EMPTY 
						: value.toString());
			}
		}
	}
}
