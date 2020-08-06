package com.maoxiong.youtu.entity.request.impl;

import java.io.UnsupportedEncodingException;

import com.maoxiong.youtu.enums.ModelType;
import com.maoxiong.youtu.enums.VoiceSpeed;

import kotlin.text.Charsets;

/**
 * 
 * @author yanrun
 *
 */
public class TextToAudioRequestEntity {

	private String text;
	private ModelType modelType;
	private VoiceSpeed speed;
	
	private static final int MAX_TEXT_LENGTH = 216;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		try {
			text = new String(text.getBytes(), Charsets.UTF_8.name());
			if (text.getBytes(Charsets.UTF_8.name()).length >= MAX_TEXT_LENGTH) {
				throw new IllegalArgumentException("text too long, max length: " + 
						MAX_TEXT_LENGTH + " bytes");
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
