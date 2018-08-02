package com.maoxiong.youtu.request;

import com.maoxiong.youtu.util.ParamUtil;

/**
 * 
 * @author yanrun
 *
 */
public interface Request {
	
	ParamUtil PARAM_UTIL = ParamUtil.getInstance();

	/**
	 * 设置请求参数
	 * @param requestEntity 请求参数
	 */
	void setParams(Object requestEntity);
	/**
	 * 获取请求参数的JSON字符串
	 * @return 请求参数的JSON字符串
	 */
	String getParamsJsonString();
	/**
	 * 获取请求的URL
	 * @return 请求的URL
	 */
	String getRequestUrl();
	
}
