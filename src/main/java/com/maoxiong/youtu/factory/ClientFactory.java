package com.maoxiong.youtu.factory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.joor.Reflect;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.enhance.EnhancedClient;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class ClientFactory {
	
	private static final Cache<String, Client> REQUEST_CACHE = Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
		    .maximumSize(16)
		    .build();
	
	private ClientFactory() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Client constructClient(Request request) {
		return new EnhancedClient(createClientByRequest(request), request);
	}
	
	private static Client createClientByRequest(Request request) {
		Objects.requireNonNull(request, "cannot create client for null request");
		Class<?> requestClass = request.getClass();
		String requestClassName = requestClass.getName();
		String cacheKey = CacheKeyUtil.generateCacheKey(requestClassName, request.getRequestUrl(), request.getParamsJsonString());
		return REQUEST_CACHE.get(cacheKey, k -> createClientByReflect(requestClass, requestClassName));
	}
	
	private static Client createClientByReflect(Class<?> requestClass, String requestClassName) {
		String requestPackage = requestClassName.substring(0, requestClassName.lastIndexOf("."));
		String simpleRequestClassName = requestClass.getSimpleName();
		String clientPackage = requestPackage.replace("request", "client");
		String clientClassName = clientPackage.concat(".").concat(simpleRequestClassName.replace("Request", "Client"));
		return (Client) Reflect.on(clientClassName).create().get();
	}
	
}
