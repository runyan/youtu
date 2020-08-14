package com.maoxiong.youtu.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author yanrun
 *
 */
public class HmacSha1 {

	private static final String HMAC_SHA1 = "HmacSHA1";

	private HmacSha1() {
		throw new RuntimeException("no constructor for you");
	}

	public static byte[] getSignature(String data, String key) {
		try {
			Mac mac = Mac.getInstance(HMAC_SHA1);
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
			mac.init(signingKey);
			return mac.doFinal(data.getBytes());
		} catch (Exception e) {
			LogUtil.error(e.getMessage());
			throw new RuntimeException("Sign error: " + e.getMessage());
		}
	}

}
