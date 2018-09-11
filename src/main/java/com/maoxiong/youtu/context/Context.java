package com.maoxiong.youtu.context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author yanrun
 *
 */
public class Context {

	private static final Map<String, Object> PARAM_MAP = new ConcurrentHashMap<>(8);
	public static final Map<String, String> JSON_MAP = new ConcurrentHashMap<>(16);
	
	private static final List<String> LEAGAL_KEYS = Arrays.asList(new String[] {"sign", "app_id", "savePath", "tempFilePath"});
	
	public static void init(String sign, String appId) {
		PARAM_MAP.put("sign", sign);
		PARAM_MAP.put("app_id", appId);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				Object pathObj = PARAM_MAP.get("tempFilePath");
				String tempFilePath = null == pathObj ? "" : String.valueOf(pathObj);
				if(!StringUtils.isBlank(tempFilePath)) {
					try {
						Files.deleteIfExists(Paths.get(tempFilePath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				clear();
			}
			
		}));
	}
	
	public static Object get(String key) {
		keyCheck(key);
		return PARAM_MAP.get(key);
	}
	
	public static void set(String key, Object value) {
		keyCheck(key);
		PARAM_MAP.put(key, value);
	}
	
	private static void clear() {
		JSON_MAP.clear();
		PARAM_MAP.clear();
	}
	
	private static void keyCheck(String key) {
		if(!LEAGAL_KEYS.contains(key)) {
			throw new IllegalArgumentException("Illegal key:" + key);
		}
	}
	
}
