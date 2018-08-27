package com.maoxiong.youtu.util.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.util.network.interceptor.HttpRetryInterceptor;
import com.maoxiong.youtu.util.network.interceptor.LogInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author yanrun
 *
 */
public class HttpUtil {
	
	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	private static final HttpRetryInterceptor RETRY_INTERCEPTOR = new HttpRetryInterceptor.Builder()
			.executionCount(5)
			.retryInterval(500)
			.build();
	private static final LogInterceptor LOG_INTERCEPTOR = new LogInterceptor();
	private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
			.retryOnConnectionFailure(true)
			.connectTimeout(3000, TimeUnit.MILLISECONDS)  
			.readTimeout(10000, TimeUnit.MILLISECONDS)
			.addInterceptor(RETRY_INTERCEPTOR)
			.addInterceptor(LOG_INTERCEPTOR)
			.build();
	
	private static final Cache<String, BaseResult> RESULT_CACHE = Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
		    .maximumSize(16)
		    .build();
	
	private HttpUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static void post(String url, String paramJson, final RequestCallback callback, Class<? extends BaseResult> responseClass) {
		String hash = String.valueOf(Objects.hash(url, paramJson));
		BaseResult resultEntity = RESULT_CACHE.getIfPresent(hash);
		if(null != resultEntity) {
			logger.info("get response from cache");
			callback.onSuccess(true, "0", new Gson().toJson(resultEntity), resultEntity);
		} else {
			realCall(hash, url, paramJson, callback, responseClass);
		}
	}
	
	private static void realCall(String hash, String url, String paramJson, final RequestCallback callback, Class<? extends BaseResult> responseClass) {
		Request request = HttpRequestBuilder.getInstance().buildRequest(url, paramJson);
		try(Response response = CLIENT.newCall(request).execute()) {
			Gson gson = new Gson();
			boolean isSuccessful = response.isSuccessful();
			if(!isSuccessful) {
				return ;
			}
			String responseBodyStr = response.body().string();
			BaseResult resultEntity = gson.fromJson(responseBodyStr, responseClass);
			String msgCode = isSuccessful ? "0" : "-1";
			String msg = resultEntity.getErrorMsg();
			if(isSuccessful) {
				RESULT_CACHE.put(hash, resultEntity);
				callback.onSuccess(isSuccessful, msgCode, responseBodyStr, gson.fromJson(responseBodyStr, responseClass));
			} else {
				callback.onFail(new RuntimeException(msg));
			}
		} catch (UnknownHostException uhe) {
			logger.error("cannot reach: " + url);
		} catch(InterruptedIOException ioe) {
			logger.error("connection interuptted: " + url);
			ioe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			callback.onFail(e);
		} 
	}
	
}


