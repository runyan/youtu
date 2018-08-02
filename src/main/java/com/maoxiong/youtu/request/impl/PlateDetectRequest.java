package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.PlateDetectRequestEntity;
import com.maoxiong.youtu.request.Request;

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
		return PARAM_UTIL.buildParamJson(entity.getFilePath(), entity.getFileUrl());
	}

	@Override
	public String getRequestUrl() {
		return url;
	}

}
