package com.maoxiong.youtu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.impl.LRUCache;

/**
 * 
 * @author yanrun
 *
 */
public class PropertyUtil {
	
	private static final LRUCache<String, Properties> PROPERTY_CACHE = new LRUCache<>(64);
	private static final String DEFAULT_PROPERTIES_FILE_PATH = "/youtu.properties";
	
	public static Properties loadProperties(String propertiesFilePath) {
		String path = StringUtils.isEmpty(propertiesFilePath) ? DEFAULT_PROPERTIES_FILE_PATH : propertiesFilePath;
		Properties props = PROPERTY_CACHE.getIfPresent(propertiesFilePath);
		if (Objects.isNull(props)) {
			props = new Properties();
			try (InputStream inputStream = PropertyUtil.class.getResourceAsStream(path)) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
	            props.load(bf);
	            PROPERTY_CACHE.set(propertiesFilePath, props);
	            return props;
			} catch(NullPointerException e) {
				LogUtil.error("Properties file: {} does not exists", propertiesFilePath);
	        } catch (IOException e){
	            LogUtil.error(e.getMessage());
	        }
			return null;
		} else {
			return props;
		}
	}
	
	public static String getPropertyValue(Properties props, String propertyKey) {
		Objects.requireNonNull(props);
		return props.getProperty(propertyKey, null);
	}
	
}
