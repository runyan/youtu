package com.maoxiong.youtu.util.configfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

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
		String path = StringUtils.isEmpty(propertiesFilePath) ? ConfigFileUtil.DEFAULT_PROPERTIES_CONFIG_FILE_PATH : propertiesFilePath;
		Properties props = ConfigFileUtil.PROPERTY_CACHE.getIfPresent(propertiesFilePath);
		if (Objects.isNull(props)) {
			props = new Properties();
			try (InputStream inputStream = PropertyUtil.class.getResourceAsStream(path)) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
	            props.load(bf);
	            ConfigFileUtil.PROPERTY_CACHE.set(propertiesFilePath, props);
	            return props;
			} catch(NullPointerException e) {
				LogUtil.error("Properties file: {} does not exists", propertiesFilePath);
//				throw new RuntimeException("Properties file: " + propertiesFilePath + " does not exists");
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
