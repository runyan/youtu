package com.maoxiong.youtu.callback;

import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public interface RequestCallback {

	void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result);
	void onFail(Exception e);
}
