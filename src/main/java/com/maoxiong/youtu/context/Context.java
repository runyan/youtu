package com.maoxiong.youtu.context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.pool.RequestPool;

/**
 * 
 * @author yanrun
 *
 */
public class Context {

	private static final Map<String, Object> PARAM_MAP = new ConcurrentHashMap<>(8);

	private static final List<String> LEAGAL_KEYS = Arrays
			.asList(new String[] { "sign", "app_id", "savePath", "tempFilePath" });
	
	private static final String NULL = "null";

	public static final Queue<RequestPool> REQUEAT_POOL_QUEUE = new ConcurrentLinkedQueue<>();

	public static void init(String sign, String appId) {
		PARAM_MAP.put("sign", sign);
		PARAM_MAP.put("app_id", appId);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				Object pathObj = PARAM_MAP.get("tempFilePath");
				String tempFilePath = Objects.isNull(pathObj) ? StringUtils.EMPTY : String.valueOf(pathObj);
				if (StringUtils.isNotBlank(tempFilePath) && !StringUtils.equalsIgnoreCase(tempFilePath, NULL)) {
					try {
						Files.deleteIfExists(Paths.get(tempFilePath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!REQUEAT_POOL_QUEUE.isEmpty()) {
					REQUEAT_POOL_QUEUE.forEach(pool -> {
						if (!pool.isClosed()) {
							pool.close();
						}
					});
				}
				PARAM_MAP.clear();
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

	private static void keyCheck(String key) {
		if (!LEAGAL_KEYS.contains(key)) {
			throw new IllegalArgumentException("Illegal key:" + key);
		}
	}

}
