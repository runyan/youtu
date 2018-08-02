package com.maoxiong.youtu.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author yanrun
 *
 */
public class BaseResult {
	
	@SerializedName(value = "errorcode")
	private String errorCode;
	@SerializedName(value = "errormsg")
	private String errorMsg;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
