package com.maoxiong.youtu.request.impl;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.FaceDectectRequestEntity;
import com.maoxiong.youtu.request.Request;

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
		return PARAM_UTIL.buildParamJson(params.getFilePath(), params.getFileUrl());
	}
	
	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
