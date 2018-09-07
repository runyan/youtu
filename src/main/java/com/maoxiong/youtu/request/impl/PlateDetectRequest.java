package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.entity.request.impl.PlateDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

public class PlateDetectRequest implements Request {

	private String url;
	private PlateDetectRequestEntity entity;
	
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
		if(Context.JSON_MAP.containsKey(cacheKey)) {
			return Context.JSON_MAP.get(cacheKey);
		} else {
			String json = PARAM_UTIL.buildParamJson(filePath, url);
			Context.JSON_MAP.put(cacheKey, json);
			return json;
		}
	}

	@Override
	public String getRequestUrl() {
		return url;
	}

}
