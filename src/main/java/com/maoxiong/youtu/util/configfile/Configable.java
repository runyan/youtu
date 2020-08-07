package com.maoxiong.youtu.util.configfile;

import java.util.Properties;

/**
 * 
 * @author yanrun
 *
 */
public interface Configable {

	/**
	 * 根据配置文件路径读取配置信息
	 * 
	 * @param filePath 配置文件路径
	 * @return 配置信息
	 */
	Properties loadProperties(String filePath);
	/**
	 * 获取优先级
	 * 
	 * @return 优先级
	 */
	int getPriority();
	/**
	 * 获取默认配置文件路径
	 * 
	 * @return 默认配置文件路径
	 */
	String getDefaultFilePath();
	
	/**
	 * 获取支持的文件类型
	 * 
	 * @return 支持的文件类型
	 */
	String getSupportFileSuffix();
}
