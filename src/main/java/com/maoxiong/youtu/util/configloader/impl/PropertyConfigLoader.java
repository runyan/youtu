package com.maoxiong.youtu.util.configloader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;
import com.maoxiong.youtu.util.LogUtil;
import com.maoxiong.youtu.util.configloader.ConfigFileLoader;

/**
 * 
 * @author yanrun
 *
 */
@ConfigLoaderConfiguration(priority = 2, suffix = "properties")
public class PropertyConfigLoader implements ConfigFileLoader {
	
	@Override
	public Properties loadProperties(String propertiesFilePath) {
		Properties props = new Properties();
		try (InputStream inputStream = PropertyConfigLoader.class.getResourceAsStream(propertiesFilePath)) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            props.load(bf);
            PROPERTY_CACHE.set(propertiesFilePath, props);
            return props;
		} catch(NullPointerException e) {
			LogUtil.warn("properties file: {} does not exists", propertiesFilePath);
			return null;
        } catch(IOException e) {
        	throw new RuntimeException(e);
        }
	}
	
	@Override
	public String getDefaultFilePath() {
		return "/youtu.properties";
	}
	
}
