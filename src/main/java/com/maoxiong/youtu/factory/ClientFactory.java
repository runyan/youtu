package com.maoxiong.youtu.factory;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class ClientFactory {

	private static final Cache<String, Client> REQUEST_CACHE = new CaffeineCache<>();

	private static Request requestDelegate;

	private ClientFactory() {
		throw new RuntimeException("no constructor for you");
	}

	public static Client constructClient(Request request) {
		Objects.requireNonNull(request, "cannot create client for null request");
		requestDelegate = request;
		return createClientByRequest();
	}

	private static Client createClientByRequest() {
		Class<?> requestClass = requestDelegate.getClass();
		String requestClassName = requestClass.getName();
		String cacheKey = CacheKeyUtil.generateCacheKey(requestClassName, requestDelegate.getRequestUrl(),
				requestDelegate.getParamsJsonString());
		return REQUEST_CACHE.get(cacheKey, k -> createClientByReflect(requestClass, requestClassName));
	}

	private static Client createClientByReflect(Class<?> requestClass, String requestClassName) {
		String requestPackage = StringUtils.substring(requestClassName, 0, StringUtils.lastIndexOf(requestClassName, "."));
		String simpleRequestClassName = requestClass.getSimpleName();
		String clientPackage = StringUtils.replace(requestPackage, "request", "client");
		String clientClassName = clientPackage.concat(".").concat(simpleRequestClassName.replace("Request", "Client"));
		try {
			return Reflect.onClass(clientClassName).create().call("setRequest", requestDelegate).get();
		} catch (Exception e) {
			throw new RuntimeException("cannot create client due to: " + e.getMessage());
		}
	}

}
