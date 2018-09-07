package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;
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
		return this.url;
	}

}
