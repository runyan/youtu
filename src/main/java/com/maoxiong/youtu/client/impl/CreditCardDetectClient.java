package com.maoxiong.youtu.client.impl;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.CorDetectResult;
import com.maoxiong.youtu.util.network.HttpUtil;

public class CreditCardDetectClient extends AbstractClient {
	
	@Override
	public void execute(CallBack callback) throws Exception {
		callbackCheck(callback);
		super.execute(callback);
		HttpUtil.post(request.getRequestUrl(), request.getParamsJsonString(), new RequestCallback() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result) {
				if(!isSuccess) {
					callback.onFail(new RuntimeException("server error"));
				}
				callback.onSuccess("0".equals(errorCode), errorCode, result.getErrorMsg(), result, result.getClass());
			}

			@Override
			public void onFail(Exception e) {
				callback.onFail(e);
			}
			
		}, CorDetectResult.class);
	}

}
