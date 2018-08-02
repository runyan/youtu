package com.maoxiong.youtu.initializer;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.util.SignUtil;

public class Initializer {
	
	private static volatile boolean isInited = false;
	
	public static void init(String userQQ, String appID, String secretID, String secretKey) {
		if(isInited) {
			throw new RuntimeException("should not init more than once");
		}
		if(StringUtils.isBlank(userQQ)) {
			throw new IllegalArgumentException("need QQ");
		}
		if(StringUtils.isBlank(appID)) {
			throw new IllegalArgumentException("need appID");
		}
		if(StringUtils.isBlank(secretID)) {
			throw new IllegalArgumentException("need secretID");
		}
		if(StringUtils.isBlank(secretKey)) {
			throw new IllegalArgumentException("need secretKey");
		}
		Context.init(SignUtil.getSign(userQQ, appID, secretID, secretKey), appID);
		isInited = true;
	}
}
