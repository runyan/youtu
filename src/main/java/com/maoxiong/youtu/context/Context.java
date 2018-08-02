package com.maoxiong.youtu.context;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.maoxiong.youtu.cache.LRUCache;
import com.maoxiong.youtu.client.Client;

/**
 * 
 * @author yanrun
 *
 */
public class Context {

	private static final Map<String, Object> PARAM_MAP = new ConcurrentHashMap<>(4);
	public static final int MAX_THREAD_NUM = 50;
	public static final LRUCache<String, Client> KNOWN_REQUESTS = new LRUCache<>(16);
	public static final Set<String> THREAD_SET = new HashSet<>(MAX_THREAD_NUM);
	
	public static void init(String sign, String appId) {
		PARAM_MAP.put("sign", sign);
		PARAM_MAP.put("app_id", appId);
	}
	
	public static Object get(String key) {
		keyCheck(key);
		return PARAM_MAP.get(key);
	}
	
	public static void set(String key, Object value) {
		keyCheck(key);
		PARAM_MAP.put(key, value);
	}
	
	private static void keyCheck(String key) {
		final String signKeyName = "sign";
		final String appIdKeyName = "app_id";
		if(!signKeyName.equals(key) && !appIdKeyName.equals(key)) {
			throw new IllegalArgumentException("Illegal key");
		}
	}
	
}
