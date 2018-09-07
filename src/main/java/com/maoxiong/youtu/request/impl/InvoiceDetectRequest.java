package com.maoxiong.youtu.request.impl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.entity.request.impl.InvoiceDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;
import com.maoxiong.youtu.util.FileUtil;
import com.maoxiong.youtu.util.ParamUtil;

public class InvoiceDetectRequest implements Request {
	
	private InvoiceDetectRequestEntity params;
	private String url;

	@Override
	public void setParams(Object requestEntity) {
		this.params = (InvoiceDetectRequestEntity) requestEntity;
		this.url = Constants.BASE_URL + "ocrapi/invoiceocr"; 
	}

	@Override
	public String getParamsJsonString() {
		String className = getClass().getName();
		String url = params.getFileUrl();
		String cacheKey = CacheKeyUtil.generateCacheKey(className, url);
		if(Context.JSON_MAP.containsKey(cacheKey)) {
			return Context.JSON_MAP.get(cacheKey);
		} else {
			byte[] imgData = FileUtil.readFileByBytes(this.params.getFileUrl());
	    	String image = Base64.getEncoder().encodeToString(imgData);
			Map<String, Object> paramMap = new HashMap<>(4);
			paramMap.put("image", image);
			String json = ParamUtil.getInstance().buildParamJson(paramMap);
			Context.JSON_MAP.put(cacheKey, json);
			return json;
		}
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
