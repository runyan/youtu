package com.maoxiong.youtu.util.network;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequestBuilder {
	
	private HttpRequestBuilder() {
	}
	
	public static HttpRequestBuilder getInstance() {
		return SingletonHolder.INSTANCE.getBuilder();
	}
	
	enum SingletonHolder {
		INSTANCE;
		
		HttpRequestBuilder builder;
		
		SingletonHolder() {
			builder = new HttpRequestBuilder();
		}
		
		public HttpRequestBuilder getBuilder() {
			return builder;
		}
	}
	
	public Request buildRequest(String url, String paramJson) {
		if(!StringUtils.startsWithIgnoreCase(url, Constants.BASE_URL)) {
			throw new IllegalArgumentException("url must starts with: " + Constants.BASE_URL);
		}
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, paramJson);
		Request request = new Request.Builder()
	            .url(url)
	            .header("Host", Constants.HOST)
	            .header("Content-Length", String.valueOf(paramJson.length()))
	            .header("Content-Type", "text/json")
	            .header("Authorization", String.valueOf(Context.get("sign")))
	            .removeHeader("User-Agent")
	            .addHeader("User-Agent", 
	            		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36")
	            .post(body)
	            .build();
		return request;
	}
}
