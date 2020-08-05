package com.maoxiong.youtu.util.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.util.CacheKeyUtil;
import com.maoxiong.youtu.util.LogUtil;
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

	private static final HttpRetryInterceptor RETRY_INTERCEPTOR = new HttpRetryInterceptor.Builder().executionCount(5)
			.retryInterval(500).build();

	private static final LogInterceptor LOG_INTERCEPTOR = new LogInterceptor();

	private static final OkHttpClient CLIENT = new OkHttpClient.Builder().retryOnConnectionFailure(true)
			.connectTimeout(3000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS)
			.addInterceptor(RETRY_INTERCEPTOR).addInterceptor(LOG_INTERCEPTOR).build();

	private static final Cache<String, BaseResult> RESULT_CACHE = new CaffeineCache<>();

	private HttpUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static void post(String url, String paramJson, RequestCallback callback,
			Class<? extends BaseResult> responseClass) {
		String cacheKey = CacheKeyUtil.generateCacheKey(url, paramJson);
		BaseResult resultEntity = RESULT_CACHE.getIfPresent(cacheKey);
		if (!Objects.isNull(resultEntity)) {
			LogUtil.info("get response from cache");
			callback.onSuccess(true, "0", new Gson().toJson(resultEntity), resultEntity);
		} else {
			realCall(cacheKey, url, paramJson, callback, responseClass);
		}
	}

	private static void realCall(String cacheKey, String url, String paramJson, RequestCallback callback,
			Class<? extends BaseResult> responseClass) {
		Gson gson = new Gson();
		Request request = HttpRequestBuilder.getInstance().buildRequest(url, paramJson);
		try (Response response = CLIENT.newCall(request).execute()) {
			boolean isSuccessful = response.isSuccessful();
			if (!isSuccessful) {
				LogUtil.warn("request to: {} failed", url);
				return;
			}
			String responseBodyStr = response.body().string();
			BaseResult resultEntity = gson.fromJson(responseBodyStr, responseClass);
			String msgCode = isSuccessful ? "0" : "-1";
			String msg = resultEntity.getErrorMsg();
			if (isSuccessful) {
				RESULT_CACHE.set(cacheKey, resultEntity);
				callback.onSuccess(isSuccessful, msgCode, responseBodyStr, resultEntity);
			} else {
				callback.onFail(new RuntimeException(msg));
			}
		} catch (UnknownHostException uhe) {
			LogUtil.error("cannot reach: {}", url);
		} catch (InterruptedIOException ioe) {
			LogUtil.error("connection interuptted: {}", url);
			ioe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			callback.onFail(e);
		}
	}

}
