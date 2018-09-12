package com.maoxiong.youtu.request.impl;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.LRUCache;
import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.FaceDectectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class FaceDetectRequest implements Request {
	
	private FaceDectectRequestEntity params;
	private String url;
	
	private static Cache<String, String> cache = new LRUCache<>(16);

	@Override
	public void setParams(Object requestEntity) {
		this.params = (FaceDectectRequestEntity) requestEntity;
		this.url = Constants.BASE_URL + "api/detectface";
	}

	@Override
	public String getParamsJsonString() {
		String filePath = params.getFilePath();
		String url = params.getFileUrl();
		String cacheKey = CacheKeyUtil.generateCacheKey(filePath, url);
		String jsonStr = cache.getIfPresent(cacheKey);
		if(StringUtils.isBlank(jsonStr)) {
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
