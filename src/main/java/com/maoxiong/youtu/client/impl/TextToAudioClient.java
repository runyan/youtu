package com.maoxiong.youtu.client.impl;

import org.apache.commons.codec.binary.Base64;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.callback.RequestCallback;
import com.maoxiong.youtu.client.AbstractClient;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.entity.result.impl.TextToAudioResult;
import com.maoxiong.youtu.request.impl.TextToAudioRequest;
import com.maoxiong.youtu.util.FileUtil;
import com.maoxiong.youtu.util.network.HttpUtil;

/**
 * 
 * @author yanrun
 *
 */
public class TextToAudioClient extends AbstractClient {
	
	private boolean shouldSaveToFile;

	@Override
	public void execute(CallBack callback) throws Exception {
		TextToAudioRequest textToAudioRequest = (TextToAudioRequest) request;
		shouldSaveToFile = textToAudioRequest.shouldSaveToFile();
		HttpUtil.post(textToAudioRequest.getRequestUrl(), textToAudioRequest.getParamsJsonString(), new RequestCallback() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result) {
				if(!isSuccess) {
					callback.onFail(new RuntimeException("server error"));
				}
				TextToAudioResult textToAudioResult = (TextToAudioResult) result;
				if(shouldSaveToFile) {
					textToAudioResult.setVoice(FileUtil.genFileFromBytes(Base64.decodeBase64(textToAudioResult.getVoice()), ".mp3"));
				}
				callback.onSuccess("0".equals(errorCode), errorCode, result.getErrorMsg(), textToAudioResult, textToAudioResult.getClass());
			}

			@Override
			public void onFail(Exception e) {
				callback.onFail(e);
			}
			
		}, TextToAudioResult.class);
	}

}
