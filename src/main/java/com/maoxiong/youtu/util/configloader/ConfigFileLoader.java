package com.maoxiong.youtu.util.configloader;

import java.util.Properties;

import com.maoxiong.youtu.cache.impl.LruCache;

/**
 * 
 * @author yanrun
 *
 */
public interface ConfigFileLoader {
	
	static final LruCache<String, Properties> PROPERTY_CACHE = new LruCache<>(64);

	/**
	 * 根据配置文件路径读取配置信息
	 * 
	 * @param filePath 配置文件路径
	 * @return 配置信息
	 */
	Properties loadProperties(String filePath);
	/**
	 * 获取默认配置文件路径
	 * 
	 * @return 默认配置文件路径
	 */
	String getDefaultFilePath();
	
}
