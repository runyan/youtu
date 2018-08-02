package com.maoxiong.youtu.client;

import java.util.Optional;

import com.maoxiong.youtu.request.Request;

public abstract class AbstractClient implements Client {
	
	protected Request request;
	
	@Override
	public Request getRequest() {
		return request;
	}
	
	@Override
	public void setRequest(Request request) {
		request = Optional.ofNullable(request)
				.orElseThrow(() -> new IllegalArgumentException("cannot set null request"));
		this.request = request;
	}

}
