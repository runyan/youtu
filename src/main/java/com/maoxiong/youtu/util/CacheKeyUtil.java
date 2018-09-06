package com.maoxiong.youtu.util;

import java.util.Base64;

public class CacheKeyUtil {
	
	private CacheKeyUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static String generateCacheKey(String... params) {
		if(null == params || params.length == 0) {
			return "";
		}
		StringBuilder strBuilder = new StringBuilder();
		int index = 0;
		int paramLength = params.length;
		for(String str : params) {
			strBuilder.append(str);
			if(index != paramLength - 1) {
				strBuilder.append(",");
			}
			index++;
		}
		return Base64.getEncoder().encodeToString(strBuilder.toString().getBytes());
	}
}
