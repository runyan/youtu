package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.CreditCradDetectRequestEntity;
import com.maoxiong.youtu.request.Request;

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
		return PARAM_UTIL.buildParamJson(entity.getFilePath(), entity.getFileUrl());
	}

	@Override
	public String getRequestUrl() {
		return url;
	}

}
