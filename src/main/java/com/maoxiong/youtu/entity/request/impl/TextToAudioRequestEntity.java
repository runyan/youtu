package com.maoxiong.youtu.entity.request.impl;

import com.maoxiong.youtu.enums.ModelType;
import com.maoxiong.youtu.enums.VoiceSpeed;

public class TextToAudioRequestEntity {

	private String text;
	private ModelType modelType;
	private VoiceSpeed speed;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ModelType getModelType() {
		return modelType;
	}
	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}
	public VoiceSpeed getSpeed() {
		return speed;
	}
	public void setSpeed(VoiceSpeed speed) {
		this.speed = speed;
	}
	
}
