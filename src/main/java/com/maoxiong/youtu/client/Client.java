package com.maoxiong.youtu.client;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.initializer.Initializer;
import com.maoxiong.youtu.request.Request;

/**
 * 
 * @author yanrun
 *
 */
public interface Client {

	/**
	 * 执行请求
	 * @param callback 回调
	 * @throws Exception 异常
	 */
	default void execute(CallBack callback) throws Exception {
		Initializer.initCheck();
	}
	
	void setRequest(Request request);
	
	Request getRequest();
}
