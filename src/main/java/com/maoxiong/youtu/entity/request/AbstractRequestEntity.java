package com.maoxiong.youtu.entity.request;

/**
 * 
 * @author yanrun
 *
 */
public abstract class AbstractRequestEntity implements BaseRequest {

	protected String filePath;
	protected String fileUrl;
	
	/**
	 * 获取请求文件路径
	 * @return 请求文件路径
	 */
	protected abstract String getFilePath();
	/**
	 * 设置请求文件路径
	 * @param path 请求文件路径
	 */
	protected abstract void setFilePath(String path);
	/**
	 * 设置请求文件URL
	 * @return 请求文件URL
	 */
	protected abstract String getFileUrl();
	/**
	 * 获取请求文件URL
	 * @param url 请求文件URL
	 */
	protected abstract void setFileUrl(String url);
	
}
