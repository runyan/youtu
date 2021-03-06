package com.maoxiong.youtu.client.impl;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.InvoiceDetectResult;
import com.maoxiong.youtu.util.network.HttpUtil;

/**
 * 
 * @author yanrun
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class InvoiceDetectClient extends AbstractClient {

	@Override
	public void execute(CallBack callback) throws Exception {
		super.execute(callback);
		HttpUtil.post(request.getRequestUrl(), request.getParamsJsonString(), new RequestCallback() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result) {
				callback.onSuccess(isSuccess, errorCode, errorMsg, result);
			}

			@Override
			public void onFail(Exception e) {
				callback.onFail(e);
			}

		}, InvoiceDetectResult.class);
	}

}
