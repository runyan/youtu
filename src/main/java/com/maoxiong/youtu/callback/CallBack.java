package com.maoxiong.youtu.callback;

import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 * @param <T> type of the returned object
 */
public interface CallBack<T extends BaseResult> {

	/**
	 * 成功回调
	 * @param isSuccess 是否成功
	 * @param errorCode 返回码
	 * @param errorMsg 返回信息
	 * @param result 返回实体

	 */
	void onSuccess(boolean isSuccess, String errorCode, String errorMsg, T result);
	/**
	 * 失败回调
	 * @param e 异常
	 */
	void onFail(Exception e);
}
