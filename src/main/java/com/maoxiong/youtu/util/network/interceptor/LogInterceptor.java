package com.maoxiong.youtu.util.network.interceptor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

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
			if (logger.isDebugEnabled()) {
                logger.debug("[--> {} {} {} ({}ms)]", requestMethod, responseCode, requestUrl, responseTime);
                logger.debug("[--> body: {}]", requestString);
                logger.debug("[<-- resp: {}]\n", responseBodyStr);
            }
		} else {
			logger.warn("[--> {} {} {} ({}ms)]", requestMethod, responseCode, requestUrl, responseTime);
            logger.warn("[--> body: {}]", requestString);
            logger.warn("[<-- resp: {}]\n", responseBodyStr);
		}
		return response;
	}

}
