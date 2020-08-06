package com.maoxiong.youtu.util.configfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class PropertyUtil {
	
	private PropertyUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Properties loadProperties(String propertiesFilePath) {
		Properties props = new Properties();
		try (InputStream inputStream = PropertyUtil.class.getResourceAsStream(propertiesFilePath)) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            props.load(bf);
            ConfigFileUtil.PROPERTY_CACHE.set(propertiesFilePath, props);
            return props;
		} catch(NullPointerException | IOException e) {
            LogUtil.error(e.getMessage());
            return null;
        }
	}
	
	public static String getPropertyValue(Properties props, String propertyKey) {
		Objects.requireNonNull(props);
		return props.getProperty(propertyKey, null);
	}
	
}
