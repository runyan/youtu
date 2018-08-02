package com.maoxiong.youtu.enhance;

import java.util.Objects;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.request.Request;

public class EnhancedClient implements Client {
	
	private Client client;
	private Request request;
	
	public EnhancedClient(Client client, Request request) {
		Objects.requireNonNull(client, "client is null");
		Objects.requireNonNull(request, "request is null");
		this.client = client;
		this.request = request;
	}

	@Override
	public void execute(CallBack callback) throws Exception {
		Objects.requireNonNull(callback, "callback is null");
		client.setRequest(request);
		client.execute(callback);
	}

	@Override
	public void setRequest(Request request) {
		if(!Objects.equals(this.request, request)) {
			throw new IllegalArgumentException("different request");
		}
		this.request = request;
	}

	@Override
	public Request getRequest() {
		return this.request;
	}
	

}
