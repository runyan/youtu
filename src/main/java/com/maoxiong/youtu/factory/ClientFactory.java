package com.maoxiong.youtu.factory;

import java.util.Objects;

import com.maoxiong.youtu.cache.LRUCache;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.enhance.EnhancedClient;
import com.maoxiong.youtu.request.Request;

/**
 * 
 * @author yanrun
 *
 */
public class ClientFactory {
	
	private static final LRUCache<String, Client> REQUEST_CACHE = Context.KNOWN_REQUESTS;

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
		String hash = String.valueOf(Objects.hash(requestClassName, request.getRequestUrl(), request.getParamsJsonString()));
		if(REQUEST_CACHE.containsKey(hash)) {
			return REQUEST_CACHE.get(hash);
		}
		String requestPackage = requestClassName.substring(0, requestClassName.lastIndexOf("."));
		String simpleRequestClassName = requestClass.getSimpleName();
		String clientPackage = requestPackage.replace("request", "client");
		String clientClassName = clientPackage.concat(".").concat(simpleRequestClassName.replace("Request", "Client"));
		Client client = null;
		try {
			Class<?> clientClass = Class.forName(clientClassName);
			client = (Client) clientClass.newInstance();
			REQUEST_CACHE.put(hash, client);
			return client;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Illegal request: " + requestClassName);
		}
	}
	
}
