package com.maoxiong.youtu.client;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.request.Request;

public abstract class AbstractClient implements Client {
	
	protected Request request;
	
	@Override
	public Request getRequest() {
		return request;
	}
	
	@Override
	public void execute(CallBack callback) throws Exception {
		Object signObj = Context.get("sign");
		signObj = null == signObj ? "" : signObj;
		String sign = String.valueOf(signObj);
		if(StringUtils.isBlank(sign)) {
			throw new IllegalStateException("have not init properly");
		}
	}
	
	@Override
	public void setRequest(Request request) {
		Objects.requireNonNull(request, "cannot set null request");
		this.request = request;
	}
	
	protected void callbackCheck(CallBack callback) {
		Objects.requireNonNull(callback, "callback cannot be null");
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(request.getRequestUrl(), request.getParamsJsonString());
	}
	
	@Override
	public boolean equals(Object anotherClient) {
		if(null == anotherClient) {
			return false;
		}
		if(!(anotherClient instanceof Client && anotherClient instanceof AbstractClient )) {
			return false;
		}
		String url = request.getRequestUrl();
		String param = request.getParamsJsonString();
		Client another = (Client) anotherClient;
		Request anotherRequest = another.getRequest();
		if(null == anotherRequest) {
			return false;
		}
		String anotherUrl = anotherRequest.getRequestUrl();
		String anotherParam = anotherRequest.getParamsJsonString();
		return url.equalsIgnoreCase(anotherUrl) && param.equalsIgnoreCase(anotherParam);
	}

}
