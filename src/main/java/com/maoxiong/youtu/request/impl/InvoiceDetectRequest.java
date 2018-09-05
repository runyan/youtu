package com.maoxiong.youtu.request.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.Base64;

import com.maoxiong.youtu.constants.Constants;
import com.maoxiong.youtu.entity.request.impl.InvoiceDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
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
		byte[] imgData = null;
		try {
			imgData = FileUtil.readFileByBytes(this.params.getFileUrl());
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	String image = Base64.getEncoder().encodeToString(imgData);
		Map<String, Object> paramMap = new HashMap<>(4);
		paramMap.put("image", image);
		return ParamUtil.getInstance().buildParamJson(paramMap);
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
