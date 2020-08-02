package com.maoxiong.youtu.entity.result.impl;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.maoxiong.youtu.entity.result.basic.InvoiceItem;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public class InvoiceDetectResult extends BaseResult {
	
	@SerializedName(value = "session_id")
	private String sessionId;
	private List<InvoiceItem> items;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public List<InvoiceItem> getItems() {
		return items;
	}
	public void setItems(List<InvoiceItem> items) {
		this.items = items;
	}
	
}
