package com.maoxiong.youtu.util.configfile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.impl.LruCache;
import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigFileUtil {

	static final LruCache<String, Properties> PROPERTY_CACHE = new LruCache<>(64);
	
	private static final Map<String, Class<?>> UTIL_MAP = new LinkedHashMap<>(16);
	private static final Map<String, String> DEFAULT_PATH = new LinkedHashMap<>(16); 
	
	static {
		UTIL_MAP.put("yml", YamlUtil.class);
		UTIL_MAP.put("properties", PropertyUtil.class);
		DEFAULT_PATH.put("yml", "youtu.yml");
		DEFAULT_PATH.put("properties", "/youtu.properties");
	}
	
	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Properties loadProperties(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return loadFromDefaultFilePath();
		}
		return loadPropertiesByReflect(filePath);
	}
	
	private static Properties loadFromDefaultFilePath() {
		String defaultPathKey;
		String defaultPath;
		Properties defaultProperties;
		for (Map.Entry<String, String> pathSet : DEFAULT_PATH.entrySet()) {
			defaultPathKey = pathSet.getKey();
			defaultPath = DEFAULT_PATH.get(defaultPathKey);
			defaultProperties = loadPropertiesByReflect(defaultPath);
			if (!Objects.isNull(defaultProperties)) {
				return defaultProperties;
			}
		}
		throw new RuntimeException("no config file path provided, nor default config files provided");
	}
	
	private static Properties loadPropertiesByReflect(String filePath) {
		int last = StringUtils.lastIndexOf(filePath, ".");
		String suffix = StringUtils.substring(filePath, last + 1);
		Class<?> utilClass = UTIL_MAP.get(suffix);
		if (Objects.isNull(utilClass)) {
			throw new IllegalArgumentException("unsupportted file type");
		}
		try {
			Method loadMethod = utilClass.getMethod("loadProperties", String.class);
			Properties props = (Properties) loadMethod.invoke(null, filePath);
			return props;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LogUtil.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
}
