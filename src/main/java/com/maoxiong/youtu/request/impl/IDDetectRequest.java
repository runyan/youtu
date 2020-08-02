package com.maoxiong.youtu.request.impl;

import java.util.HashMap;
import java.util.Map;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.IDDetectRequestEntity;
import com.maoxiong.youtu.request.Request;

/**
 * 
 * @author yanrun
 *
 */
public class IDDetectRequest implements Request {

	private String url;
	private IDDetectRequestEntity entity;
	private Map<String, Object> paramMap = new HashMap<>(8);

	@Override
	public void setParams(Object requestEntity) {
		this.url = Constants.BASE_URL + "ocrapi/idcardocr";
		this.entity = (IDDetectRequestEntity) requestEntity;
	}

	@Override
	public String getParamsJsonString() {
		synchronized (paramMap) {
			paramMap.put("url", entity.getFileUrl());
			paramMap.put("image", entity.getFilePath());
			paramMap.put("card_type", entity.getCardType().getCardTypeValue());
			paramMap.put("border_check_flag", entity.getBorderCheckFlag());
		}
		return PARAM_UTIL.buildParamJson(paramMap);
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}
	
}
