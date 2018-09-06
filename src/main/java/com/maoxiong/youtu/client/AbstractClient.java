package com.maoxiong.youtu.client;

import java.util.Objects;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.request.Request;

public abstract class AbstractClient implements Client {
	
	protected Request request;
	
	@Override
	public Request getRequest() {
		return request;
	}
	
	@Override
	public void setRequest(Request request) {
		Objects.requireNonNull(request, "cannot set null request");
		this.request = request;
	}
	
	protected void callbackCheck(CallBack callback) {
		Objects.requireNonNull(callback, "callback cannot be null");
	}

}
