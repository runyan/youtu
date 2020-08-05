package com.maoxiong.youtu.util.network;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 
 * @author yanrun
 *
 */
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
		try {
			URL targetUrl = new URL(url);
			String host = targetUrl.getHost();
			String protocol = targetUrl.getProtocol();
			if (!StringUtils.equalsIgnoreCase(protocol, Constants.PROTOCOL)) {
				throw new IllegalArgumentException("wrong protocol, must be: " + Constants.PROTOCOL);
			}
			if (!StringUtils.equalsIgnoreCase(host, Constants.HOST)) {
				throw new IllegalArgumentException("wrong host, must be: " + Constants.HOST);
			}
			MediaType JSON = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(paramJson, JSON);
			Headers.Builder headersBuilder = new Headers.Builder();
			Headers headers = headersBuilder.add("Host", Constants.HOST)
					.add("Content-Length", String.valueOf(paramJson.length())).add("Content-Type", "text/json")
					.add("Authorization", String.valueOf(Context.get("sign")))
					.add("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36")
					.build();
			Request request = new Request.Builder().url(targetUrl).headers(headers).post(body).build();
			return request;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("malformed url:" + url);
		}
	}
}
