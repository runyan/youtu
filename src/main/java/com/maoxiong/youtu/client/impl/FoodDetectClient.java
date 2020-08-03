package com.maoxiong.youtu.client.impl;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.FoodDetectResult;
import com.maoxiong.youtu.util.network.HttpUtil;

/**
 * 
 * @author yanrun
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class FoodDetectClient extends AbstractClient {

	@Override
	public void execute(CallBack callback) throws Exception {
		super.execute(callback);
		HttpUtil.post(request.getRequestUrl(), request.getParamsJsonString(), new RequestCallback() {
			
			public void onFail(Exception e) {
				callback.onFail(e);
			}


			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg,
					BaseResult responseEntity) {
				if(!isSuccess) {
					callback.onFail(new RuntimeException("server error"));
				}
				callback.onSuccess(StringUtils.equals("0", errorCode), errorCode, responseEntity.getErrorMsg(), responseEntity);
			}
		}, FoodDetectResult.class);  
	}

}
