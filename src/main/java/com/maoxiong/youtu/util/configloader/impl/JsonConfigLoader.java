package com.maoxiong.youtu.util.configloader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.maoxiong.youtu.annotation.ConfigLoaderConfiguration;
import com.maoxiong.youtu.util.LogUtil;
import com.maoxiong.youtu.util.configloader.entity.JsonConfigEntity;

/**
 * 
 * @author yanrun
 *
 */
@ConfigLoaderConfiguration(priority = 3, suffixs = "json", defaultFilePath = "youtu.json")
public class JsonConfigLoader extends AbstractConfigLoader {

	@Override
	protected Properties doLoadProperties(String filePath) {
		Gson gson = new Gson();
		try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
				InputStreamReader in = new InputStreamReader(is);) {
			JsonReader reader = new JsonReader(in);
			List<JsonConfigEntity> configEntityList = gson.fromJson(reader, new TypeToken<List<JsonConfigEntity>>() {
			}.getType());
			if (Objects.isNull(configEntityList) || configEntityList.isEmpty()) {
				throw new RuntimeException("no config found in " + filePath);
			}
			Properties props = new Properties();
			for (JsonConfigEntity configEntity : configEntityList) {
				String key = configEntity.getConfigName();
				String value = configEntity.getConfigValue();
				if (StringUtils.isNotEmpty(key)) {
					props.setProperty(key, StringUtils.isEmpty(value) ? StringUtils.EMPTY : value);
				}
			}
			return props;
		} catch (NullPointerException e) {
			LogUtil.warn("json file: {} does not exists", filePath);
			return null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
