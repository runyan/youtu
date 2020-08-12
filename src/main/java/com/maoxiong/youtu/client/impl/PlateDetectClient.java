package com.maoxiong.youtu.client.impl;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.constants.HttpConstants;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.CorDetectResult;
import com.maoxiong.youtu.util.network.HttpUtil;

/**
 * 
 * @author yanrun
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class PlateDetectClient extends AbstractClient {

	@Override
	public void execute(CallBack callback) throws Exception {
		super.execute(callback);
		HttpUtil.post(request.getRequestUrl(), request.getParamsJsonString(), new RequestCallback() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result) {
				if (!isSuccess) {
					callback.onFail(new RuntimeException("server error"));
				}
				callback.onSuccess(StringUtils.equals(HttpConstants.RESPONSE_SUCCESS_CODE, errorCode), errorCode, result.getErrorMsg(), result);
			}

			@Override
			public void onFail(Exception e) {
				callback.onFail(e);
			}

		}, CorDetectResult.class);
	}

}
