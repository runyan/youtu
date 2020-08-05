package com.maoxiong.youtu.util;

/**
 * 
 * @author yanrun
 *
 */
public class CommonUtil {
	
	private CommonUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static String singularOrPlural(int num, String singular, String plural) {
		return num <= 1 ? singular : plural;
	}
}
