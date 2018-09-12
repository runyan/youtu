package com.maoxiong.youtu.client.impl;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.FaceDetectResult;
import com.maoxiong.youtu.util.network.HttpUtil;

/**
 * 
 * @author yanrun
 *
 */
public class FaceDetectClient extends AbstractClient {
	
	@Override
	public void execute(CallBack callback) throws Exception {
		super.execute(callback);
		HttpUtil.post(request.getRequestUrl(), request.getParamsJsonString(), new RequestCallback() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult responseEntity) {
				callback.onSuccess(isSuccess, responseEntity.getErrorCode(), responseEntity.getErrorMsg(), responseEntity, responseEntity.getClass());
			}

			@Override
			public void onFail(Exception e) {
				callback.onFail(e);
			}
			
		}, FaceDetectResult.class);
	}

}
