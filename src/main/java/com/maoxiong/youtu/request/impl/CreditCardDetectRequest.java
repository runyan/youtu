package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.entity.request.impl.CreditCradDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;

public class CreditCardDetectRequest implements Request {
	
	private String url;
	private CreditCradDetectRequestEntity entity;

	@Override
	public void setParams(Object requestEntity) {
		this.url = Constants.BASE_URL + "ocrapi/creditcardocr";
		this.entity = (CreditCradDetectRequestEntity) requestEntity;
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
