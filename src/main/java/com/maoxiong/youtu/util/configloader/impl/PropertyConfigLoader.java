package com.maoxiong.youtu.util.configloader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;
import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
@ConfigLoaderConfiguration(priority = 2, suffixs = "properties", defaultFilePath = "/youtu.properties")
public class PropertyConfigLoader extends AbstractConfigLoader {
	
	@Override
	public Properties doLoadProperties(String propertiesFilePath) {
		Properties props = new Properties();
		try (InputStream inputStream = PropertyConfigLoader.class.getResourceAsStream(propertiesFilePath)) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            props.load(bf);
            return props;
		} catch(NullPointerException e) {
			LogUtil.warn("properties file: {} does not exists", propertiesFilePath);
			return null;
        } catch(IOException e) {
        	throw new RuntimeException(e);
        }
	}
	
}
