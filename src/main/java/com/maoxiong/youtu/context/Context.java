package com.maoxiong.youtu.context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author yanrun
 *
 */
public class Context {

	private static final Map<String, Object> PARAM_MAP = new ConcurrentHashMap<>(8);
	private static final List<String> LEAGAL_KEYS = Arrays.asList(new String[] {"sign", "app_id", "savePath"});
	
	public static final Map<String, String> JSON_MAP = new ConcurrentHashMap<>(16);
	
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
	
	public static void clear() {
		PARAM_MAP.clear();
	}
	
	private static void keyCheck(String key) {
		if(!LEAGAL_KEYS.contains(key)) {
			throw new IllegalArgumentException("Illegal key:" + key);
		}
	}
	
}
