package com.maoxiong.youtu.util.configloader.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;
import com.maoxiong.youtu.util.configloader.ConfigFileLoader;

/**
 * 
 * @author yanrun
 *
 */
public abstract class AbstractConfigLoader implements ConfigFileLoader {
	
	protected static final Cache<String, Properties> PROPERTY_CACHE = new CaffeineCache<>();
	private static final Set<String> VISITED_NULL_PROPERTY_PATH = new HashSet<>(16);
	
	@Override
	public Properties loadProperties(String filePath) {
		Properties props = PROPERTY_CACHE.getIfPresent(filePath);
		if (Objects.isNull(props)) {
			if (VISITED_NULL_PROPERTY_PATH.contains(filePath)) {
				return null;
			}
			props = doLoadProperties(filePath);
			if (Objects.nonNull(props)) {
				PROPERTY_CACHE.set(filePath, props);
			} else {
				VISITED_NULL_PROPERTY_PATH.add(filePath);
			}
		}
		return props;
	}
	
	/**
	 * 真正读取配置
	 * 
	 * @param filePath 配置文件路径
	 * @return 配置
	 */
	protected abstract Properties doLoadProperties(String filePath);
}
