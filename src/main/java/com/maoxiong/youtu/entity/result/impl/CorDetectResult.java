package com.maoxiong.youtu.entity.result.impl;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.basic.Item;

/**
 * 
 * @author yanrun
 *
 */
public class CorDetectResult extends BaseResult {

	@SerializedName(value = "session_id")
	private String sessionId;
	private List<Item> items;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
