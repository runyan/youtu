package com.maoxiong.youtu.request.impl;

import java.util.HashMap;
import java.util.Map;

import com.maoxiong.youtu.constants.HttpConstants;
import com.maoxiong.youtu.entity.request.impl.TextToAudioRequestEntity;
import com.maoxiong.youtu.request.Request;

/**
 * 
 * @author yanrun
 *
 */
public class TextToAudioRequest implements Request {

	private String url;
	private TextToAudioRequestEntity entity;
	private Map<String, Object> paramMap = new HashMap<>(8);
	private boolean shouldSaveToFile;

	public TextToAudioRequest() {
		this(false);
	}

	public TextToAudioRequest(boolean shouldSaveToFile) {
		this.shouldSaveToFile = shouldSaveToFile;
	}

	@Override
	public void setParams(Object requestEntity) {
		entity = (TextToAudioRequestEntity) requestEntity;
		url = HttpConstants.BASE_URL + "ttsapi/text_to_audio";
	}

	@Override
	public String getParamsJsonString() {
		synchronized (paramMap) {
			paramMap.put("text", entity.getText());
			paramMap.put("model_type", entity.getModelType().getModelType());
			paramMap.put("speed", entity.getSpeed().getSpeed());
		}
		return PARAM_UTIL.buildParamJson(paramMap);
	}

	@Override
	public String getRequestUrl() {
		return url;
	}

	public boolean shouldSaveToFile() {
		return shouldSaveToFile;
	}

}
