package com.maoxiong.youtu.request.impl;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.LRUCache;
import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.PlateDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class PlateDetectRequest implements Request {

	private String url;
	private PlateDetectRequestEntity entity;

	private Cache<String, String> cache = new LRUCache<>(16);

	@Override
	public void setParams(Object requestEntity) {
		this.entity = (PlateDetectRequestEntity) requestEntity;
		this.url = Constants.BASE_URL + "ocrapi/plateocr";
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
		return url;
	}

}
