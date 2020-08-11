package com.maoxiong.youtu.util.configloader.impl;

import java.util.Objects;
import java.util.Properties;

import com.maoxiong.youtu.cache.impl.LruCache;
import com.maoxiong.youtu.util.configloader.ConfigFileLoader;

/**
 * 
 * @author yanrun
 *
 */
public abstract class AbstractConfigLoader implements ConfigFileLoader {
	
	protected static final LruCache<String, Properties> PROPERTY_CACHE = new LruCache<>(64);

	@Override
	public Properties loadProperties(String filePath) {
		Properties props = PROPERTY_CACHE.getIfPresent(filePath);
		if (Objects.isNull(props)) {
			props = doLoadProperties(filePath);
			if (Objects.nonNull(props)) {
				PROPERTY_CACHE.set(filePath, props);
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
