package com.maoxiong.youtu.util.network.interceptor;

import java.io.IOException;

import com.maoxiong.youtu.util.LogUtil;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 
 * @author yanrun
 *
 */
public class LogInterceptor implements Interceptor {
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		Response response = chain.proceed(request);
		long responseTime = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
		String requestString = "";
		RequestBody requestBody = request.body();
		if(null != requestBody) {
			Buffer buffer = new Buffer();
			requestBody.writeTo(buffer);
			requestString = buffer.readUtf8();
		}
		ResponseBody responseBody = response.peekBody(1024 * 1024);
		String requestMethod = request.method();
		int responseCode = response.code();
		String requestUrl = request.url().toString();
		String responseBodyStr = responseBody.string();
		if(response.isSuccessful()) {
			if (LogUtil.isDebugEnabled()) {
				LogUtil.debug("[--> {} {} {} ({}ms)]", requestMethod, responseCode, requestUrl, responseTime);
				LogUtil.debug("[--> body: {}]", requestString);
				LogUtil.debug("[<-- resp: {}]\n", responseBodyStr);
            }
		} else {
			LogUtil.warn("[--> {} {} {} ({}ms)]", requestMethod, responseCode, requestUrl, responseTime);
			LogUtil.warn("[--> body: {}]", requestString);
			LogUtil.warn("[<-- resp: {}]\n", responseBodyStr);
		}
		return response;
	}

}
