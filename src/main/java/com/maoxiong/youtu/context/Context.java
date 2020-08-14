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

import com.maoxiong.youtu.constants.ContextConstants;
import com.maoxiong.youtu.pool.RequestPool;
import com.maoxiong.youtu.util.ExceptionUtil;
import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class Context {

	private static final Map<String, Object> PARAM_MAP = new ConcurrentHashMap<>(8);

	private static final List<String> LEAGAL_KEYS = Arrays
			.asList(new String[] { ContextConstants.SIGN, ContextConstants.APP_ID, 
					ContextConstants.SAVE_PATH, ContextConstants.TEMP_FILE_SAVE_PATH });
	
	private static final String NULL = "null";

	public static final Queue<RequestPool> REQUEST_POOL_QUEUE = new ConcurrentLinkedQueue<>();

	public static void init(String sign, String appId) {
		PARAM_MAP.put(ContextConstants.SIGN, sign);
		PARAM_MAP.put(ContextConstants.APP_ID, appId);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Object pathObj = PARAM_MAP.get(ContextConstants.TEMP_FILE_SAVE_PATH);
			String tempFilePath = Objects.isNull(pathObj) ? StringUtils.EMPTY : String.valueOf(pathObj);
			if (StringUtils.isNotBlank(tempFilePath) && !StringUtils.equalsIgnoreCase(tempFilePath, NULL)) {
				try {
					Files.deleteIfExists(Paths.get(tempFilePath));
				} catch (IOException e) {
					LogUtil.warn(ExceptionUtil.getExceptionStackTrace(e));
				}
			}
			while (!REQUEST_POOL_QUEUE.isEmpty()) {
				RequestPool pool = REQUEST_POOL_QUEUE.poll();
				if (!pool.isClosed()) {
					pool.close();
				}
			}
			PARAM_MAP.clear();
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
