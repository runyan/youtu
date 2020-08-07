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
public class YamlUtil implements Configable {
	
	@Override
	public Properties loadProperties(String yamlFilePath) {
		Yaml yml = new Yaml();
		Properties props = new Properties();
		try (InputStream inputStream = YamlUtil.class
				  .getClassLoader()
				  .getResourceAsStream(yamlFilePath)) {
			Map<String, Object> propertyMap = yml.load(inputStream);
			if (Objects.isNull(propertyMap)) {
				return null;
			}
			parseMap(propertyMap, StringUtils.EMPTY, props);
			ConfigFileUtil.PROPERTY_CACHE.set(yamlFilePath, props);
			return props;
		} catch (YAMLException | IOException e) {
			LogUtil.error(e.getMessage());
			return null;
		}
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
	public int getPriority() {
		return 1;
	}
	
	@Override
	public String getDefaultFilePath() {
		return "youtu.yml";
	}
	
	@Override 
	public String getSupportFileSuffix() {
		return "yml";
	}

}
