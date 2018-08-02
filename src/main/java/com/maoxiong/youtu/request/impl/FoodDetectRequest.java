package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.FoodDetectRequestEntity;
import com.maoxiong.youtu.request.Request;

public class FoodDetectRequest implements Request {
	
	private String url;
	private FoodDetectRequestEntity entity;

	@Override
	public void setParams(Object requestEntity) {
		this.url = Constants.BASE_URL + "imageapi/fooddetect";
		this.entity = (FoodDetectRequestEntity) requestEntity;
	}

	@Override
	public String getParamsJsonString() {
		return PARAM_UTIL.buildParamJson(entity.getFilePath(), entity.getFileUrl());
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
