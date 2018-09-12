package com.maoxiong.youtu.client;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.context.Context;
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
		Objects.requireNonNull(callback, "callback cannot be null");
		Object signObj = Context.get("sign");
		signObj = null == signObj ? "" : signObj;
		String sign = String.valueOf(signObj);
		if(StringUtils.isBlank(sign)) {
			throw new IllegalStateException("have not init properly");
		}
	}
	
	void setRequest(Request request);
	
	Request getRequest();
}
