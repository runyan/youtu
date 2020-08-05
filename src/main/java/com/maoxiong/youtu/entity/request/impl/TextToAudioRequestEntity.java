package com.maoxiong.youtu.entity.request.impl;

import java.io.UnsupportedEncodingException;

import com.maoxiong.youtu.enums.ModelType;
import com.maoxiong.youtu.enums.VoiceSpeed;

/**
 * 
 * @author yanrun
 *
 */
public class TextToAudioRequestEntity {

	private String text;
	private ModelType modelType;
	private VoiceSpeed speed;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		try {
			text = new String(text.getBytes(), "utf-8");
			if (text.getBytes("utf-8").length >= 216) {
				throw new IllegalArgumentException("text too long, max length: 216 bytes");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
