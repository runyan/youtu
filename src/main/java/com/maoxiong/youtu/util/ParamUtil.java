package com.maoxiong.youtu.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.maoxiong.youtu.constants.ContextConstants;
import com.maoxiong.youtu.context.Context;

/**
 * 
 * @author yanrun
 *
 */
public class ParamUtil {

	private static final Map<String, Object> PARAM_MAP = new HashMap<>(8);

	private static final String APP_ID = String.valueOf(Context.get(ContextConstants.APP_ID));

	private ParamUtil() {}

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
		if (!PARAM_MAP.isEmpty()) {
			PARAM_MAP.clear();
		}
		if (StringUtils.isNotBlank(url)) {
			PARAM_MAP.put("url", url);
			return buildParamJson(PARAM_MAP);
		}
		if (StringUtils.isNotBlank(filePath)) {
			byte[] imgData = FileUtil.readFileByBytes(filePath);
			String image = Base64.getEncoder().encodeToString(imgData);
			PARAM_MAP.put("image", image);
			return buildParamJson(PARAM_MAP);
		}
		throw new NullPointerException("need param url or path");
	}

	public String buildParamJson(Map<String, Object> params) {
		Objects.requireNonNull(params, "empty param");
		params.put(ContextConstants.APP_ID, APP_ID);
		Gson gson = new Gson();
		return gson.toJson(params);
	}

}
