package com.maoxiong.youtu.entity.result.impl;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.maoxiong.youtu.entity.result.basic.FaceItem;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public class FaceDetectResult extends BaseResult {

	@SerializedName(value = "session_id")
	private String sessionId;
	@SerializedName(value = "image_height")
	private Integer imageHeight;
	@SerializedName(value = "image_width")
	private Integer imageWidth;
	@SerializedName(value = "face")
	private List<FaceItem> faceItemList;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public List<FaceItem> getFaceItemList() {
		return faceItemList;
	}
	public void setFaceItemList(List<FaceItem> face) {
		this.faceItemList = face;
	}
}
