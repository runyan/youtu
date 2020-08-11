package com.maoxiong.youtu.request.impl;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;
import com.maoxiong.youtu.constants.HttpConstants;
import com.maoxiong.youtu.entity.request.impl.FoodDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class FoodDetectRequest implements Request {

	private String url;
	private FoodDetectRequestEntity entity;

	private Cache<String, String> cache = new CaffeineCache<>();

	@Override
	public void setParams(Object requestEntity) {
		this.url = HttpConstants.BASE_URL + "imageapi/fooddetect";
		this.entity = (FoodDetectRequestEntity) requestEntity;
	}

	@Override
	public String getParamsJsonString() {
		String filePath = entity.getFilePath();
		String url = entity.getFileUrl();
		String cacheKey = CacheKeyUtil.generateCacheKey(filePath, url);
		String jsonStr = cache.getIfPresent(cacheKey);
		if (StringUtils.isBlank(jsonStr)) {
			jsonStr = PARAM_UTIL.buildParamJson(filePath, url);
			cache.set(cacheKey, jsonStr);
		}
		return jsonStr;
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
