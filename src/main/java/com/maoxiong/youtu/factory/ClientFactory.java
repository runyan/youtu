package com.maoxiong.youtu.factory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.joor.Reflect;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.maoxiong.youtu.client.Client;
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
	
	private static Request internalRequest;
	
	private ClientFactory() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Client constructClient(Request request) {
		Objects.requireNonNull(request, "cannot create client for null request");
		internalRequest = request;
		return createClientByRequest(request);
	}
	
	private static Client createClientByRequest(Request request) {
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
		try {
			return (Client) Reflect.on(clientClassName).create()
					.call("setRequest", internalRequest).get();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("cannot create client due to: " + e.getMessage());
		}
	}
	
}
