package com.maoxiong.youtu.pool.impl;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.UniqueIDUtil;

/**
 * 
 * @author yanrun
 *
 */
class RequestWrapper implements Comparable<RequestWrapper> {

	private long id;
	private Request request;
	private Client requestClient;
	private CallBack<? extends BaseResult> callback;
	
	private String requestUrl;
	private String requestParam;
	
	public RequestWrapper(Client requestClient, CallBack<? extends BaseResult> callback) {
		Objects.requireNonNull(requestClient, "null requestClient");
		Objects.requireNonNull(callback, "null callback");
		this.id = new UniqueIDUtil(0, 0).nextId();
		this.request = Optional.ofNullable(requestClient.getRequest())
				.orElseThrow(() -> new RuntimeException("have not set request for client:" + requestClient.getClass().getName()));
		this.requestClient = requestClient;
		this.callback = callback;
		this.requestUrl = request.getRequestUrl();
		this.requestParam = request.getParamsJsonString();
	}
	
	public long getId() {
		return id;
	}
	
	public Request getRequest() {
		return request;
	}
	public Client getRequestClient() {
		return requestClient;
	}
	public CallBack<? extends BaseResult> getCallback() {
		return callback;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(request, requestClient, callback);
	}
	
	@Override
	public boolean equals(Object another) {
		if(null == another) {
			return false;
		}
		if(getClass() != another.getClass() || !(another instanceof RequestWrapper)) {
			return false;
		}
		RequestWrapper anotherWrapper = (RequestWrapper) another;
		Request anotherRequest = anotherWrapper.getRequest();
		String anotherUrl = anotherRequest.getRequestUrl();
		String anotherParam = anotherRequest.getParamsJsonString();
			return StringUtils.equalsIgnoreCase(requestUrl, anotherUrl) && StringUtils.equalsIgnoreCase(requestParam, anotherParam);
	}
	
	@Override
	public String toString() {
		return "request url: " + requestUrl + " param: " + requestParam;
	}

	@Override
	public int compareTo(RequestWrapper another) {
		if(this.equals(another)) {
			return 0;
		}
		return Integer.compare(this.hashCode(), another.hashCode());
	}
}
