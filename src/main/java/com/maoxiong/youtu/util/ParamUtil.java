package com.maoxiong.youtu.util;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.maoxiong.youtu.context.Context;

/**
 * 
 * @author yanrun
 *
 */
public class ParamUtil {

	private static final Logger logger = LogManager.getLogger(ParamUtil.class);
	
	private static final Map<String, Object> paramMap = new HashMap<>(8);
	
	private final String appId = String.valueOf(Context.get("app_id"));

	private ParamUtil() {
	}
	
	enum SingletonHolder {
		/**
		 * instance
		 */
		INSTANCE;
		
		ParamUtil util;
		
		SingletonHolder() {
			this.util = new ParamUtil();
		}
		
		public ParamUtil getUtil() {
			return util;
		}
	}
	
	public static ParamUtil getInstance() {
		return SingletonHolder.INSTANCE.getUtil();
	}
	
	public synchronized String buildParamJson(String filePath, String url) {
		if(!paramMap.isEmpty()) {
			paramMap.clear();
		}
		if(!StringUtils.isBlank(url)) {
			paramMap.put("url", url);
			return buildParamJson(paramMap);
		}
		if(!StringUtils.isBlank(filePath)) {
			byte[] imgData = null;
			try {
				imgData = FileUtil.readFileByBytes(filePath);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("read file error, path:" + filePath);
			} 
	    	String image = Base64.getEncoder().encodeToString(imgData);
	    	paramMap.put("image", image);
			return buildParamJson(paramMap);
		}
		throw new NullPointerException("need param url or path");
	}
	
	public String buildParamJson(Map<String, Object> params) {
		Objects.requireNonNull(params, "empty param");
		params.put("app_id", appId);
		Gson gson = new Gson();
		return gson.toJson(params);
	}
	
}
