package com.maoxiong.youtu.util.configloader;

import java.util.Properties;

/**
 * 
 * @author yanrun
 *
 */
public interface ConfigFileLoader {
	
	/**
	 * 根据配置文件路径读取配置信息
	 * 
	 * @param filePath 配置文件路径
	 * @return 配置信息
	 */
	Properties loadProperties(String filePath);
	
}
