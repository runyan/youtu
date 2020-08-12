package com.maoxiong.youtu.constants;

/**
 * 
 * @author yanrun
 *
 */
public interface HttpConstants {

	static final String PROTOCOL = "https";
	static final String HOST = "api.youtu.qq.com";
	static final String PATH = "/youtu/";
	static final String BASE_URL = PROTOCOL + "://" + HOST + PATH;
	
	static final String RESPONSE_SUCCESS_CODE = "0";
	static final String RESPONSE_FAIL_CODE = "-1";
}
